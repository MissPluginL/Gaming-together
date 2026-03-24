# ============================================================================
# Gamerr Backend API 全功能测试脚本
# 依赖：后端服务运行于 http://localhost:8080/api
#       数据库已初始化且可连接
# 用法：PowerShell -File test-api.ps1
# ============================================================================

$BASE = "http://localhost:8080/api"
$PASS = "PASS"
$FAIL = "FAIL"
$total = 0; $passed = 0; $failed = 0
$results = @()

function Get-RandomUsername {
    "testuser_$(Get-Random -Minimum 1000 -Maximum 99999)"
}
function Test-Api {
    param($Name, $ScriptBlock)
    $script:total++
    try {
        $r = & $ScriptBlock
        if ($r.ok) {
            $script:passed++
            $script:results += "✅ [$PASS] $Name"
            Write-Host "[PASS] $Name" -ForegroundColor Green
            return $true
        } else {
            $script:failed++
            $script:results += "❌ [$FAIL] $Name | $($r.msg)"
            Write-Host "[FAIL] $Name : $($r.msg)" -ForegroundColor Red
            return $false
        }
    } catch {
        $script:failed++
        $msg = $_.Exception.Message
        $script:results += "❌ [$FAIL] $Name | $msg"
        Write-Host "[FAIL] $Name : $msg" -ForegroundColor Red
        return $false
    }
}

Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  Gamerr Backend API 全功能测试" -ForegroundColor Cyan
Write-Host "========================================`n" -ForegroundColor Cyan

# ============================================================
# 模块 1：用户认证 (5 个测试)
# ============================================================
Write-Host "[模块 1] 用户认证" -ForegroundColor Yellow

# T1-1 注册 - 成功
$T1_1_USER = Get-RandomUsername
Test-Api "T1-1 注册成功" {
    $body = @{
        username = $T1_1_USER
        password = "Test123456"
        nickname = "测试用户"
        email    = "$T1_1_USER@test.com"
        phone    = "138$(Get-Random -Minimum 10000000 -Maximum 99999999)"
    } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "$BASE/users/register" -Method Post -ContentType "application/json" -Body $body
    if ($r.code -eq 200) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg=$r.message} }
}

# T1-2 注册 - 用户名已存在
Test-Api "T1-2 注册失败-用户名重复" {
    $body = @{ username = $T1_1_USER; password = "Test123456"; nickname = "重复"; email = "dup@test.com" } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "$BASE/users/register" -Method Post -ContentType "application/json" -Body $body
    if ($r.code -ne 200) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg="应返回错误但返回成功"} }
}

# T1-3 登录 - 成功
$T1_3_USER = $T1_1_USER
Test-Api "T1-3 登录成功" {
    $body = @{ username = $T1_3_USER; password = "Test123456" } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "$BASE/users/login" -Method Post -ContentType "application/json" -Body $body
    if ($r.code -eq 200 -and $r.data.token) {
        $script:TOKEN = $r.data.token
        $script:USER_ID = $r.data.userId
        @{ok=$true; msg=$r.message}
    } else { @{ok=$false; msg=$r.message} }
}

# T1-4 登录 - 密码错误
Test-Api "T1-4 登录失败-密码错误" {
    $body = @{ username = $T1_3_USER; password = "WrongPass123" } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "$BASE/users/login" -Method Post -ContentType "application/json" -Body $body
    if ($r.code -ne 200) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg="应返回错误但返回成功"} }
}

# T1-5 重置密码
Test-Api "T1-5 重置密码-验证通过" {
    $body = @{ username = $T1_3_USER; phone = "13800000000"; code = "123456"; newPassword = "NewPass789" } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "$BASE/users/reset-password" -Method Post -ContentType "application/json" -Body $body
    if ($r.code -eq 200) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg=$r.message} }
}

# ============================================================
# 模块 2：用户管理 (4 个测试)
# ============================================================
Write-Host "`n[模块 2] 用户管理" -ForegroundColor Yellow

# T2-1 获取个人资料
Test-Api "T2-1 获取个人资料" {
    $headers = @{ "X-User-Id" = $script:USER_ID; "Authorization" = "Bearer $($script:TOKEN)" }
    $r = Invoke-RestMethod -Uri "$BASE/users/info" -Method Get -Headers $headers
    if ($r.code -eq 200 -and $r.data.username -eq $T1_3_USER) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg=$r.message} }
}

# T2-2 更新个人资料
Test-Api "T2-2 更新个人资料" {
    $headers = @{ "X-User-Id" = $script:USER_ID; "Authorization" = "Bearer $($script:TOKEN)" }
    $body = @{ nickname = "新昵称_$(Get-Random)"; gameRank = "钻石"; skillTags = "ADC,辅助" } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "$BASE/users/info" -Method Put -Headers $headers -ContentType "application/json" -Body $body
    if ($r.code -eq 200) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg=$r.message} }
}

# T2-3 获取指定用户资料
Test-Api "T2-3 获取他人资料" {
    $r = Invoke-RestMethod -Uri "$BASE/users/$($script:USER_ID)" -Method Get
    if ($r.code -eq 200 -and $r.data.id -eq $script:USER_ID) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg=$r.message} }
}

# T2-4 搜索用户
Test-Api "T2-4 搜索用户" {
    $r = Invoke-RestMethod -Uri "$BASE/users/search?keyword=$T1_3_USER" -Method Get
    if ($r.code -eq 200 -and $r.data -ne $null) { @{ok=$true; msg="$($r.data.Count) 条结果"} } else { @{ok=$false; msg=$r.message} }
}

# ============================================================
# 模块 3：游戏管理 (3 个测试)
# ============================================================
Write-Host "`n[模块 3] 游戏管理" -ForegroundColor Yellow

# T3-1 分页获取游戏列表
Test-Api "T3-1 分页获取游戏列表" {
    $r = Invoke-RestMethod -Uri "$BASE/games?page=1&size=5" -Method Get
    if ($r.code -eq 200 -and $r.data.records.Count -gt 0) { @{ok=$true; msg="$($r.data.total) 条记录"} } else { @{ok=$false; msg=$r.message} }
}

# T3-2 获取游戏详情
Test-Api "T3-2 获取游戏详情" {
    $r = Invoke-RestMethod -Uri "$BASE/games/1" -Method Get
    if ($r.code -eq 200 -and $r.data.id -eq 1) { @{ok=$true; msg=$r.data.name} } else { @{ok=$false; msg=$r.message} }
}

# T3-3 获取全部游戏
Test-Api "T3-3 获取全部游戏" {
    $r = Invoke-RestMethod -Uri "$BASE/games/all" -Method Get
    if ($r.code -eq 200 -and $r.data.Count -gt 0) { @{ok=$true; msg="$($r.data.Count) 款游戏"} } else { @{ok=$false; msg=$r.message} }
}

# ============================================================
# 模块 4：论坛系统 (3 个测试)
# ============================================================
Write-Host "`n[模块 4] 论坛系统" -ForegroundColor Yellow

# T4-1 发帖
Test-Api "T4-1 发帖成功" {
    $headers = @{ "X-User-Id" = $script:USER_ID }
    $body = @{ title = "测试帖子_$(Get-Random)"; content = "这是测试帖子的内容，测试发帖功能。"; gameId = 1 } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "$BASE/forum/posts" -Method Post -Headers $headers -ContentType "application/json" -Body $body
    if ($r.code -eq 200 -and $r.data.id -gt 0) {
        $script:POST_ID = $r.data.id
        @{ok=$true; msg="帖子ID=$($r.data.id)"}
    } else { @{ok=$false; msg=$r.message} }
}

# T4-2 回复帖子
Test-Api "T4-2 回复帖子" {
    $headers = @{ "X-User-Id" = $script:USER_ID }
    $body = @{ content = "这是测试回复内容，测试回复功能。" } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "$BASE/forum/posts/$($script:POST_ID)/replies" -Method Post -Headers $headers -ContentType "application/json" -Body $body
    if ($r.code -eq 200) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg=$r.message} }
}

# T4-3 删除帖子
Test-Api "T4-3 删除自己发的帖子" {
    $headers = @{ "X-User-Id" = $script:USER_ID }
    $r = Invoke-RestMethod -Uri "$BASE/forum/posts/$($script:POST_ID)" -Method Delete -Headers $headers
    if ($r.code -eq 200) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg=$r.message} }
}

# ============================================================
# 模块 5：服务订单 (4 个测试)
# ============================================================
Write-Host "`n[模块 5] 服务订单" -ForegroundColor Yellow

# T5-1 创建订单
Test-Api "T5-1 创建服务订单" {
    $headers = @{ "X-User-Id" = $script:USER_ID }
    $body = @{
        gameName    = "英雄联盟"
        title       = "测试订单_$(Get-Random)"
        description = "测试订单描述内容"
        budget      = 100
    } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "$BASE/orders" -Method Post -Headers $headers -ContentType "application/json" -Body $body
    if ($r.code -eq 200 -and $r.data.id -gt 0) {
        $script:ORDER_ID = $r.data.id
        @{ok=$true; msg="订单ID=$($r.data.id)"}
    } else { @{ok=$false; msg=$r.message} }
}

# T5-2 订单支付
Test-Api "T5-2 订单支付" {
    $headers = @{ "X-User-Id" = $script:USER_ID }
    $r = Invoke-RestMethod -Uri "$BASE/orders/$($script:ORDER_ID)/pay" -Method Post -Headers $headers
    if ($r.code -eq 200) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg=$r.message} }
}

# T5-3 接单
Test-Api "T5-3 接取订单" {
    $headers = @{ "X-User-Id" = $script:USER_ID }
    $r = Invoke-RestMethod -Uri "$BASE/orders/$($script:ORDER_ID)/accept" -Method Post -Headers $headers
    if ($r.code -eq 200) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg=$r.message} }
}

# T5-4 取消订单
Test-Api "T5-4 取消订单" {
    $headers = @{ "X-User-Id" = $script:USER_ID }
    $r = Invoke-RestMethod -Uri "$BASE/orders/$($script:ORDER_ID)/cancel" -Method Post -Headers $headers
    if ($r.code -eq 200) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg=$r.message} }
}

# ============================================================
# 模块 6：开黑房间 (4 个测试)
# ============================================================
Write-Host "`n[模块 6] 开黑房间" -ForegroundColor Yellow

# T6-1 创建房间
Test-Api "T6-1 创建开黑房间" {
    $headers = @{ "X-User-Id" = $script:USER_ID }
    $body = @{
        title       = "测试房间_$(Get-Random)"
        description = "测试房间描述"
        gameId      = 1
        maxPlayers  = 5
        roomType    = 1
    } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "$BASE/squad/rooms" -Method Post -Headers $headers -ContentType "application/json" -Body $body
    if ($r.code -eq 200 -and $r.data.id -gt 0) {
        $script:ROOM_ID = $r.data.id
        @{ok=$true; msg="房间ID=$($r.data.id)"}
    } else { @{ok=$false; msg=$r.message} }
}

# T6-2 获取房间详情
Test-Api "T6-2 获取房间详情" {
    $r = Invoke-RestMethod -Uri "$BASE/squad/rooms/$($script:ROOM_ID)" -Method Get
    if ($r.code -eq 200 -and $r.data.id -eq $script:ROOM_ID) { @{ok=$true; msg=$r.data.title} } else { @{ok=$false; msg=$r.message} }
}

# T6-3 加入房间
Test-Api "T6-3 加入房间(另一用户)" {
    # 用另一个用户ID尝试加入（但数据库可能没有，这里只测试接口可达）
    $headers = @{ "X-User-Id" = "99999" }
    $r = Invoke-RestMethod -Uri "$BASE/squad/rooms/$($script:ROOM_ID)/join" -Method Post -Headers $headers
    # 只要接口正常响应即可（可能失败但应返回有意义的错误）
    if ($r.message -ne $null) { @{ok=$true; msg="$($r.code) - $($r.message)"} } else { @{ok=$false; msg=$r.message} }
}

# T6-4 关闭房间
Test-Api "T6-4 关闭房间" {
    $headers = @{ "X-User-Id" = $script:USER_ID; "X-User-Role" = "USER" }
    $r = Invoke-RestMethod -Uri "$BASE/squad/rooms/$($script:ROOM_ID)" -Method Delete -Headers $headers
    if ($r.code -eq 200) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg=$r.message} }
}

# ============================================================
# 模块 7：预约系统 (2 个测试)
# ============================================================
Write-Host "`n[模块 7] 预约系统" -ForegroundColor Yellow

# T7-1 创建预约
Test-Api "T7-1 创建游戏预约" {
    $headers = @{ "X-User-Id" = $script:USER_ID }
    $body = @{ gameId = 1; reservationTime = "2026-04-01 20:00:00"; remark = "测试预约" } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "$BASE/reservations" -Method Post -Headers $headers -ContentType "application/json" -Body $body
    if ($r.code -eq 200) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg=$r.message} }
}

# T7-2 获取我的预约列表
Test-Api "T7-2 获取我的预约列表" {
    $headers = @{ "X-User-Id" = $script:USER_ID }
    $r = Invoke-RestMethod -Uri "$BASE/reservations/my" -Method Get -Headers $headers
    if ($r.code -eq 200) { @{ok=$true; msg="$($r.data.Count) 条预约"} } else { @{ok=$false; msg=$r.message} }
}

# ============================================================
# 模块 8：私信系统 (2 个测试)
# ============================================================
Write-Host "`n[模块 8] 私信系统" -ForegroundColor Yellow

# T8-1 发送私信
Test-Api "T8-1 发送私信" {
    $headers = @{ "X-User-Id" = $script:USER_ID }
    $body = @{ receiverId = $script:USER_ID; content = "测试私信_$(Get-Random)"; type = "TEXT" } | ConvertTo-Json
    $r = Invoke-RestMethod -Uri "$BASE/messages" -Method Post -Headers $headers -ContentType "application/json" -Body $body
    if ($r.code -eq 200) { @{ok=$true; msg=$r.message} } else { @{ok=$false; msg=$r.message} }
}

# T8-2 获取会话列表
Test-Api "T8-2 获取会话未读数" {
    $headers = @{ "X-User-Id" = $script:USER_ID }
    $r = Invoke-RestMethod -Uri "$BASE/messages/unread-count" -Method Get -Headers $headers
    if ($r.code -eq 200) { @{ok=$true; msg="未读数=$($r.data)"} } else { @{ok=$false; msg=$r.message} }
}

# ============================================================
# 汇总报告
# ============================================================
Write-Host "`n========================================" -ForegroundColor Cyan
Write-Host "  测试汇总报告" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host "总计：$total 个测试" -ForegroundColor White
Write-Host "通过：$passed 个" -ForegroundColor Green
Write-Host "失败：$failed 个" -ForegroundColor Red
Write-Host "通过率： $([math]::Round($passed/$total*100,1))%" -ForegroundColor $(if ($passed -eq $total) {"Green"} else {"Yellow"})
Write-Host ""

$failResults = $results | Where-Object { $_ -like "❌*" }
if ($failResults.Count -gt 0) {
    Write-Host "失败详情：" -ForegroundColor Red
    $failResults | ForEach-Object { Write-Host "  $_" -ForegroundColor Red }
}

Write-Host "`n详细结果已保存至: D:\Gamerr\Gamerr\test-results.txt`n"
$results | Out-File -FilePath "D:\Gamerr\Gamerr\test-results.txt" -Encoding UTF8

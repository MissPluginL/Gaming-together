import request from './request'

export function getGameList(params) {
  return request({
    url: '/games',
    method: 'get',
    params
  })
}

export function getAllGames() {
  return request({
    url: '/games/all',
    method: 'get'
  })
}

export function getGameDetail(id) {
  return request({
    url: `/games/${id}`,
    method: 'get'
  })
}

export function createGame(data) {
  return request({
    url: '/games',
    method: 'post',
    data
  })
}

export function updateGame(id, data) {
  return request({
    url: `/games/${id}`,
    method: 'put',
    data
  })
}

export function deleteGame(id) {
  return request({
    url: `/games/${id}`,
    method: 'delete'
  })
}

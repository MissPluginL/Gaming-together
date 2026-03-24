import request from './request'

export function getReservationList(params) {
  return request({
    url: '/reservations',
    method: 'get',
    params
  })
}

export function getMyReservations() {
  return request({
    url: '/reservations/my',
    method: 'get'
  })
}

export function createReservation(gameId) {
  return request({
    url: '/reservations',
    method: 'post',
    params: { gameId }
  })
}

export function cancelReservation(id) {
  return request({
    url: `/reservations/${id}`,
    method: 'delete'
  })
}

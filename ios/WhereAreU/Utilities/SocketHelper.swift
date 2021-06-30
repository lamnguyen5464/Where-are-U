////
////  WS.swift
////  Canvas-iOS
////
////  Created by Lam Nguyen on 25/06/2021.
////
//
//import Foundation
//import SocketIO
//
//class WS {
//
//    static let intance = WS()
//    var socket: SocketIOClient!
//
//    let manager = SocketManager(socketURL: URL(string: SOCKET_URI)!, config: [.log(false), .compress])
//
//    private init() {
//        socket = manager.defaultSocket
//    }
//
//    func connectSocket() {
//        disconnectSocket()
//        socket.on(clientEvent: .connect) {[weak self] (data, ack) in
//            print("socket connected")
//            self?.socket.emit("request_join", "1")
//        }
//
//        socket.connect()
//    }
//
//    func disconnectSocket() {
//        socket.removeAllHandlers()
//        socket.disconnect()
//        print("socket Disconnected")
//    }
//
//    func checkConnection() -> Bool {
//        if socket.manager?.status == .connected {
//            return true
//        }
//        return false
//
//    }
//
//    func setEventListener(eventName: String, resolve: @escaping (Any) -> Void){
//        self.socket.on(eventName, callback: {(data, ack) in
//            resolve(data[0])
//        })
//    }
//
//    enum Events {
//
//        case search
//
//        var emitterName: String {
//            switch self {
//            case .search:
//                return "emt_search_tags"
//            }
//        }
//
//        var listnerName: String {
//            switch self {
//            case .search:
//                return "filtered_tags"
//            }
//        }
//
//        //        func emit(params: [String : Any]) {
//        //            WS.instance.socket.emit(emitterName, params)
//        //        }
//        //
//        //        func listen(completion: @escaping (Any) -> Void) {
//        //            WS.instance.socket.on(listnerName) { (response, emitter) in
//        //                completion(response)
//        //            }
//        //        }
//        //
//        //        func off() {
//        //            WS.instance.socket.off(listnerName)
//        //        }
//    }
//}

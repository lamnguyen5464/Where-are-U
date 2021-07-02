import Foundation
import SocketIO
import GoogleMaps

class SocketHelper {
    
    static let intance = SocketHelper()
    var socket: SocketIOClient!
    
    let manager = SocketManager(socketURL: URL(string: SOCKET_URI)!, config: [.log(false), .compress])
    
    private init() {
        socket = manager.defaultSocket
    }
    
    func connectSocket() {
        socket.on(clientEvent: .connect) {[weak self] (data, ack) in
            print("socket connected", self?.socket.sid! ?? "")
            self?.socket.emit("request_join", "1")
        }
        
        socket.connect()
    }
    
    func disconnectSocket() {
        socket.removeAllHandlers()
        socket.disconnect()
        print("socket Disconnected")
    }
    
    func checkConnection() -> Bool {
        if socket.manager?.status == .connected {
            return true
        }
        return false
        
    }
    
    func setEventListener(eventName: String, resolve: @escaping (Any) -> Void){
        self.socket.on(eventName, callback: {(data, ack) in
            resolve(data[0])
        })
    }
    
    func emitCoordinateToServer(newCoordinate: CLLocationCoordinate2D){
        let dic = ["latitude": newCoordinate.latitude,
                   "longitude": newCoordinate.longitude,
                   "id": self.socket.sid ?? ""
        ] as [String : Any]
        
        guard let jsonData = try? JSONSerialization.data(withJSONObject: dic, options: []) else { return }
        
        print("send...", String(data: jsonData, encoding: String.Encoding.ascii)!)
        socket.emit("device_data", String(data: jsonData, encoding: String.Encoding.ascii)!)
    }
    
    
    
}

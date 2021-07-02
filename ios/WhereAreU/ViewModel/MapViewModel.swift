import Foundation
import GoogleMaps

class MapViewModel: NSObject, CLLocationManagerDelegate{
    var map: GMSMapView!
    var locationManager: CLLocationManager!
    
    init(map: GMSMapView){
        super.init()
        self.map = map
        self.locationManager = CLLocationManager()
        self.locationManager.delegate = self
        self.locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation
        
        SocketHelper.intance.connectSocket(callback: {
            self.startUpdatingLocation()
        })
        
        SocketHelper.intance.setEventListener(eventName: "server_data", resolve: {(data) in
            let dataString = data as? String ?? ""
            print(dataString)
            
            if let jsonStroke = try? JSONSerialization.jsonObject(with: Data(dataString.utf8), options: []) as? [Any] {
                
            }
        })
        
    }
    
    func startUpdatingLocation(){
        locationManager.startUpdatingLocation()
    }
    
    private func onUpdatedLocation(newCoordinate: CLLocationCoordinate2D){
        print("onUpdatedLocation", newCoordinate)
        
        let marker = GMSMarker()
        marker.position = newCoordinate// CLLocationCoordinate2D(latitude: -33.86, longitude: 151.20)
        marker.title = "I am here"
        marker.map = self.map
        
        SocketHelper.intance.emitCoordinateToServer(newCoordinate: newCoordinate)
    }
    
    
    
    // MARK: - CLLocationManagerDelegate
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        switch status {
        case .denied:
            print("denied")
            //navigate to Settings
            break
        case .restricted:
            //navigate to Settings
            print("restricted")
        case .authorizedWhenInUse, .authorizedAlways:
            print("authorizedWhenInUse")
            manager.startUpdatingLocation()
            break
        default:
            print("notDetermined")
            manager.requestAlwaysAuthorization()
            break
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        //        onUpdatedLocation(newCoordinate: manager.location!.coordinate)
        
        //test
        onUpdatedLocation(newCoordinate: CLLocationCoordinate2D(latitude: 10.84943893, longitude: 106.635764122))
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Failed \(error)")
    }
}


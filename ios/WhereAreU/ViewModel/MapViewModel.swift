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
    }
    
    func startUpdatingLocation(){
        print("ahaerha")
        locationManager.startUpdatingLocation()
    }
    
    private func onUpdatedLocation(newCoordinate: CLLocation){
        print("onUpdatedLocation", newCoordinate)
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
        onUpdatedLocation(newCoordinate: manager.location!)
        
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Failed \(error)")
    }
    
    
}



// Creates a marker in the center of the map.

//        let marker = GMSMarker()
//        marker.position = CLLocationCoordinate2D(latitude: -33.86, longitude: 151.20)
//        marker.title = "Sydney"
//        marker.snippet = "Australia"
//        marker.map = mapView







//        if CLLocationManager.locationServicesEnabled() {
//            locationManager.delegate = self
//            locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
//            locationManager.startUpdatingLocation()
//        }


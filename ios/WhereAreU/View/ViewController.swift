//
//  ViewController.swift
//  WhereAreU
//
//  Created by Lam Nguyen on 30/06/2021.
//

import UIKit
import GoogleMaps

class ViewController: UIViewController, CLLocationManagerDelegate {
    
    let locationManager = CLLocationManager()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let camera = GMSCameraPosition.camera(withLatitude: -33.86, longitude: 151.20, zoom: 6.0)
        let mapView = GMSMapView.map(withFrame: self.view.frame, camera: camera)
        self.view.addSubview(mapView)
        
        // Creates a marker in the center of the map.
        let marker = GMSMarker()
        marker.position = CLLocationCoordinate2D(latitude: -33.86, longitude: 151.20)
        marker.title = "Sydney"
        marker.snippet = "Australia"
        marker.map = mapView
        
        
        //get location
        locationManager.delegate = self
        locationManager.desiredAccuracy = kCLLocationAccuracyBestForNavigation  //or can use "kCLLocationAccuracyBest"
        
        if locationManager.authorizationStatus != .authorizedWhenInUse {
            print("@@@ test requesting")
            locationManager.requestWhenInUseAuthorization()
        } else {
            print("@@@ test okay")
            locationManager.startUpdatingLocation()
        }
        
        locationManager.startUpdatingLocation()
        
        
        //        if CLLocationManager.locationServicesEnabled() {
        //            locationManager.delegate = self
        //            locationManager.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
        //            locationManager.startUpdatingLocation()
        //        }
        
    }
    
    // MARK: - CLLocationManagerDelegate
    private func locationManager(manager: CLLocationManager, didChangeAuthorizationStatus status: CLAuthorizationStatus) {
        switch status {
        case .denied:
            print("denied")
            manager.requestAlwaysAuthorization()
            break
        case .authorizedAlways:
            print("authorizedAlways")
            break
        case .restricted:
            print("restricted")
        case .authorizedWhenInUse:
            print("authorizedWhenInUse")
            break
        default:
            print("notDetermined")
            manager.requestAlwaysAuthorization()
            break
        }
    }
    
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        print("Success \(locations.first)")
    }
    
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("Failed \(error)")
    }
    
    
    
    
}


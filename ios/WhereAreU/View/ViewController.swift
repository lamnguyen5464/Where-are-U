import UIKit
import GoogleMaps

class ViewController: UIViewController, CLLocationManagerDelegate {
    
    var mapViewModel: MapViewModel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mapViewModel = MapViewModel(map: GMSMapView.map(withFrame: self.view.frame, camera: GMSCameraPosition.camera(withLatitude: 10.847673, longitude: 106.635067, zoom: 17.0)))
        self.view.addSubview(mapViewModel.map)
        
    }
}


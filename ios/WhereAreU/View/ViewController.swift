import UIKit
import GoogleMaps

class ViewController: UIViewController, CLLocationManagerDelegate {
    
    var mapViewModel: MapViewModel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        mapViewModel = MapViewModel(map: GMSMapView.map(withFrame: self.view.frame, camera: GMSCameraPosition.camera(withLatitude: 10.847673, longitude: 106.635067, zoom: 17.0)))
        
        mapViewModel.startUpdatingLocation()
        self.view.addSubview(mapViewModel.map)
        
    }
    
    override func viewWillDisappear(_ animated: Bool){
        super.viewWillDisappear(true)
        SocketHelper.intance.disconnectSocket()
    }
}


import Foundation
import UIKit

extension CGPoint{
    public func getScaledPoint() -> CGPoint{
        let screenSize: CGRect = UIScreen.main.bounds
        if self.x < 1 && self.y < 1 {
            return  CGPoint(x: screenSize.width*self.x, y: screenSize.height*self.y)
        }
        return CGPoint(x: self.x / screenSize.width, y: self.y / screenSize.height)
    }
}

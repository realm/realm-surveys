import Foundation
import UIKit

private func fromRow(_ section: Int) -> (_ row: Int) -> IndexPath {
    return { row in
        return IndexPath(row: row, section: section)
    }
}

extension UITableView {

    func applyChanges(section: Int = 0, deletions: [Int], insertions: [Int], updates: [Int]) {
        beginUpdates()
        deleteRows(at: deletions.map(fromRow(section)), with: .automatic)
        insertRows(at: insertions.map(fromRow(section)), with: .automatic)
        reloadRows(at: updates.map(fromRow(section)), with: .none)
        endUpdates()
    }

}

/*
 * Copyright 2017 Realm Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


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

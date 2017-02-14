//
//  ViewController.swift
//  PopBoard
//
//  Created by Marin Todorov on 2/7/17.
//  Copyright © 2017 Realm Inc. All rights reserved.
//

import Cocoa

struct SurveyResult {
    let question: String
    let yesCount: Int
    let noCount: Int
}

let scores = [ SurveyResult(question: "Do you like icecream?", yesCount: 12, noCount: 2),
               SurveyResult(question: "Do you like icecream?", yesCount: 12, noCount: 2),
               SurveyResult(question: "Do you like icecream?", yesCount: 12, noCount: 2)];

class ViewController: NSViewController {
    @IBOutlet var tableView: NSTableView!
    
    var results: [SurveyResult]?
    
    override func viewDidAppear() {
        super.viewDidAppear()
        refresh()
    }

    func refresh() {
        tableView.reloadData()
        DispatchQueue.main.asyncAfter(deadline: .now()+5, execute: refresh)
    }
}

extension ViewController: NSTableViewDataSource {
    func numberOfRows(in tableView: NSTableView) -> Int {
        return scores.count
    }
}

extension ViewController: NSTableViewDelegate {
    func tableView(_ tableView: NSTableView, viewFor tableColumn: NSTableColumn?, row: Int) -> NSView? {
        
        guard let cell = tableView.make(
            withIdentifier: tableColumn!.identifier,
            owner: nil) as? NSTableCellView else {
            return nil
        }

        let score = scores[row]
        
        if tableColumn!.identifier == "question" {
            cell.textField?.stringValue = score.question
        } else if tableColumn!.identifier == "yesCount" {
            cell.textField?.stringValue = String(format: "%d", score.yesCount)
        } else if tableColumn!.identifier == "noCount" {
            cell.textField?.stringValue = String(format: "%d", score.noCount)
        }
        return cell
    }

    func tableView(_ tableView: NSTableView, heightOfRow row: Int) -> CGFloat {
        return 40.0
    }
}

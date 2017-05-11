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

package io.realm.realmsurveys.controller

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import io.realm.OrderedRealmCollection
import io.realm.RealmRecyclerViewAdapter
import io.realm.realmsurveys.R
import io.realm.realmsurveys.model.Question

class QuestionViewAdapter(
        data: OrderedRealmCollection<Question>?,
        private val surveyResponseDelegate: ((questionId: String, answer: Boolean) -> Any))
    : RealmRecyclerViewAdapter<Question, QuestionViewAdapter.ViewHolder>(data, true) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.survey_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val question = data!![position]

        holder.questionText.text = question.questionText
        holder.yesButton.isEnabled = true
        holder.noButton.isEnabled = true

        val clickListener = View.OnClickListener { v ->
            if (v === holder.yesButton) {
                holder.yesButton.isEnabled = false
                holder.noButton.isEnabled = true
            } else if (v === holder.noButton) {
                holder.noButton.isEnabled = false
                holder.yesButton.isEnabled = true
            }

            surveyResponseDelegate(question.questionId!!, v === holder.yesButton)
        }

        holder.yesButton.setOnClickListener(clickListener)
        holder.noButton.setOnClickListener(clickListener)
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val questionText: TextView
        val yesButton: Button
        val noButton: Button

        init {
            questionText = view.findViewById(R.id.questionText) as TextView
            yesButton = view.findViewById(R.id.yesBtn) as Button
            noButton = view.findViewById(R.id.noBtn) as Button
        }

    }

}

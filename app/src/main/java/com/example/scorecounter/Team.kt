package com.example.scorecounter

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Team(var name: String, var colorResource: Int, var score: Int = 0) : Parcelable
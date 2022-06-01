package com.example.historyvideo.base

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<V : BaseViewModel, B : ViewDataBinding> : Fragment() {
}
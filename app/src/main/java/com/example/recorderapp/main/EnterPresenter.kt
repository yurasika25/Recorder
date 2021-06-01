package com.example.recorderapp.main

import com.example.recorderapp.mvp.BasicPresenter

class EnterPresenter : BasicPresenter<EnterView?>()  {

    override fun onEnterScope() {
        super.onEnterScope()
        getView()?.goToMainFragment()
    }
}


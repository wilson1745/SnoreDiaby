package com.bristol.snorediaby.web.view.activities.calculate;

import com.bristol.snorediaby.R;
import com.bristol.snorediaby.web.view.activities.calculate.mvp.CalculateModel;

/**
 * Function: CalculatePresenter
 * Description:
 * Author: wilso
 * Date: 2022/5/7
 * MaintenancePersonnel: wilso
 */
public class CalculatePresenter {

    private CalculateActivity view;

    private CalculateModel model;

    public CalculatePresenter(CalculateActivity view, CalculateModel model) {
        this.view = view;
        this.model = model;
    }


    public CalculateActivity getView() {
        return view;
    }

    public void setView(CalculateActivity view) {
        this.view = view;
    }

    public CalculateModel getModel() {
        return model;
    }

    public void setModel(CalculateModel model) {
        this.model = model;
    }

}

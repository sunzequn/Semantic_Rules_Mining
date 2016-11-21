package com.sunzequn.srm.core;

import com.sunzequn.srm.bean.Pattern;
import com.sunzequn.srm.bean.Rule;
import com.sunzequn.srm.query.KConnectivityQuery;

/**
 * Created by Sloriac on 2016/11/21.
 */
public class TransationHandler {

    private KConnectivityQuery kConnectivityQuery = new KConnectivityQuery();

    /**
     * @param pattern
     * @param mode    1:只挖掘头实体；2只挖掘规则体实体；3：都挖掘
     * @return
     */
    public Rule frequentItemsMining(Pattern pattern, int mode) {

        return null;
    }
}

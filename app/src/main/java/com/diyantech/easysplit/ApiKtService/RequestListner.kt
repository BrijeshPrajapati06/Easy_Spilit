package com.diyantech.easysplit.ApiKtService

interface RequestListner {


    /**
     * Called when a request completes with the given response. Executed by a
     * background thread: do not update the UI in this method.
     */
    fun onComplete(requestCode: RequestCode?, `object`: Any?, message: String?)

    /**
     * Called when a request has a network or request error. Executed by a
     * background thread: do not update the UI in this method.
     */
    fun onException(error: String?, requestCode: RequestCode?)

    /**
     * This method called when a request needs to recall
     *
     * @param requestCode (RequestCode) : To identify the request type
     */
    fun onRetryRequest(requestCode: RequestCode?)

    /**
     * This method called when a request has a network or request error. Executed by a
     * background thread.
     * <pre>`status = 1  // response status success
     * status = 2  // response status fail
     * status = 3  // response status error
     * status = 4  // response status invalid
    ` *
    </pre> *
     *
     * @param error       (String)            : to pass the required error e.g. network error, request error
     * @param status      (int)           : to get the response status
     * @param requestCode (RequestCode) : to identify the response
     */
    fun onRequestError(error: String?, status: Int, requestCode: RequestCode?)

}
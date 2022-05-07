package com.bristol.snorediaby.common.enums;

public enum ErrorMessageEnum implements BaseMessage {

    /** 格式錯誤! */
    FORMAT_FAIL("格式錯誤"),

    /** 畫面初始化失敗! */
    INIT_FAIL("畫面初始化失敗"),

    /**
     * 必填未填
     */
    REQUIRED_NULL("請填寫 %s"),

    /**
     * 長度超過
     */
    MAX_LENGTH("欄位: %s超過允許上限值"),

    /**
     * 格式有誤
     */
    FORMAT_ERROR("欄位: %s格式有誤"),

    /**
     * [刪除]按鈕
     */
    DELETE_DETAIL("是否刪除第%d筆的明細?"),

    /** [暫存]按鈕-失敗 */
    TEMP_SAVE_FAIL("暫存失敗"),

    /** [儲存]按鈕-失敗 */
    SAVE_FAIL("儲存失敗"),

    /** [儲存]按鈕-失敗，還原【取消指派】動作 */
    SAVE_FAIL_UNDO_CANCEL_ASSIGN("儲存失敗，還原【取消指派】動作"),

    /**
     * 申請表單送出簽核成功
     */
    SIGN_OFF_SUCCESS("送簽成功。申請單編號為%s"),

    /** 待辦-簽核失敗 */
    FLOW_SIGN_FAIL("簽核失敗"),

    /** 取得表單失敗 */
    GET_FORM_FAIL("取得表單失敗"),

    /** 檢視歷程失敗 */
    VIEW_HISTORY_FAIL("檢視歷程失敗"),

    /** 刪除失敗 */
    DELETE_FAIL("刪除失敗"),

    /** 取得待辦失敗 */
    GET_TODOS_FAIL("取得待辦資料失敗"),

    /** 系統錯誤 */
    SYSTEM_ERROR("系統錯誤"),

    /** 送出失敗 */
    SUBMIT_FAIL("送出失敗"),

    /** 送出變更申請失敗 */
    SUBMIT_EXE_FAIL("送出變更申請失敗"),

    /** 檔案刪除失敗 */
    FILE_DEL_FAIL("檔案刪除失敗!"),

    /** 檔案上傳失敗 */
    UPLOAD_FILE_FAIL("檔案上傳失敗!"),

    /** 檔案過大 */
    VALIDATE_FILE_SIZE("檔案超過%dMB"),

    /** 檔名過長 */
    FILE_NAME_TOO_LONG("檔名超過%d個字"),

    /** 檔名不可含特殊字元 */
    FILE_NAME_NO_SPECIAL_CHARACTERS("檔案名稱不可含特殊字元%s"),

    /** 上傳檔案類型不符 */
    FILE_EXTENDSION_INCOMPATIBLE("上傳檔案類型不符"),

    /** 檔案儲存失敗 */
    FILE_SAVE_FAIL("檔案儲存失敗"),

    /** 檔案還在上傳中 */
    FILE_UPLOADING("檔案還在上傳中"),

    /** 請選擇上傳檔案 */
    SELECT_FILE_FAIL("請選擇上傳檔"),

    /** 專案完工報告必須有上傳檔案，才能送審 */
    SELECT_FILE_NEED("有上傳檔案，才能送審"),

    /** 新增失敗 */
    ADD_FAIL("新增失敗"),

    /** 請選擇部門 */
    DEPT_SEL_FAIL("請選擇部門"),

    /** 取消指派失敗，還原【取消指派】動作 */
    CANCEL_ASSIGN_FAIL("取消指派失敗，還原【取消指派】動作"),

    /** 複製失敗 */
    COPY_FAIL("複製失敗"),

    /** 開啟連結失敗 */
    OPEN_URL_FAIL("開啟連結失敗"),

    /** 請輸入工時. */
    WORK_HOURS_NO("請輸入工時"),

    /** 工時錯誤 */
    WORK_HOURS_FAIL("工時錯誤"),

    /** 每天工時不可超過12小時 */
    WORK_HOURS_DAY_THEN_MAX("每天工時不可超過12小時"),

    /** 每週工時不可超過72小時 */
    WORK_HOURS_WEEK_THEN_MAX("每週工時不可超過72小時"),

    /** 未填寫工時項目 */
    WORK_HOURS_ITEM_NULL("未填寫工時項目"),

    /** 未填寫工時子分類/專案/客戶項目 */
    WORK_HOURS_SUB_ITEM_NULL("未填寫工時子分類/專案/客戶項目"),

    /** 工時分類/子分類重覆 */
    WORK_HOURS_ITEM_DUPLICATE("工時分類/子分類重覆"),

    /** 篩選失敗 */
    FILTER_FAIL("篩選失敗"),

    /** 請選擇客戶 */
    SELECT_CUSTOMER("請選擇客戶"),

    /** 請選擇專案 */
    SELECT_PROJ("請選擇專案"),

    /** 選擇專案失敗 */
    PROJ_SELECT_FAIL("選擇專案失敗"),

    /** 查詢失敗 */
    QUERY_FAIL("查詢失敗"),

    /** 清除失敗 */
    CLEAN_FAIL("清除失敗"),

    /** 匯出失敗 */
    EXPORT_FAIL("匯出失敗"),

    /** 下載失敗 */
    DOWNLOAD_FAIL("下載失敗"),

    /** 檔案不存在 */
    FILE_IS_NOT_EXIST("檔案不存在"),

    /**
     * 起日大於迄日
     */
    STARTDATE_BIG_THEN_ENDDATE("%s起日大於迄日"),

    /** 資料重覆 */
    DUPLICATE_DATA("資料重覆"),

    /**
     * 資料為空值
     */
    DATA_EMPTY("欄位: %s資料為空值"),

    /**
     * 長度過長
     */
    VALIDTE_LENGTH("欄位: %s長度超過%d個字"),

    /**
     * 數值過大
     */
    NUM_TOO_BIG("欄位: %s數值過大"),

    /** 已指派的成員中，只能有一位[%s] */
    ROLE_DUPLICATE("已指派的成員中，只能有一位[%s]"),

    /** 已指派的成員中，至少有一位[%s] */
    ROLE_AT_LEAST_ONE("已指派的成員中，至少有一位[%s]"),

    /** 您並非[%s]部門主管，不可新增此部門成員 */
    HAS_NO_DEPT_PERMIT_ADD("您並非[%s]部門主管，不可新增此部門成員"),

    /** 您並非[%s]部門主管，不可取消指派此部門成員 */
    HAS_NO_DEPT_PERMIT_CANCEL("您並非[%s]部門主管，不可取消指派此部門成員"),

    /** 產生廠商驗收單失敗 */
    GEN_ACCEPT_FAIL("產生廠商驗收單失敗"),

    /** 計算[整體評分]失敗 */
    CAL_EVAL_SCORE_TOTAL_FAIL("計算[整體評分]失敗"),

    /** 計算[人月總計]失敗 */
    CAL_EVAL_MAN_TOTAL_FAIL("計算[人月總計]失敗"),

    /** 取得[評等/燈號]失敗 */
    GET_EVAL_SIGNAL_FAIL("取得[評等/燈號]失敗"),

    /** 取得[評估範本]失敗 */
    GET_EVAL_TMPL_FAIL("取得[評估範本]失敗"),

    /** 資料初始化失敗! */
    DATA_FAIL("資料初始化失敗"),

    /** 專案已結束 xxx專案 已於yyyy/mm/dd結束 */
    PROJECT_IS_FINISH_ARG2("%s 專案，已於 %s 結束!"),

    /** 請勾選評估項目 */
    SELECT_BOX("請勾選評估項目"),

    /** 重新取得里程資訊失敗 */
    RELOAD_EXE_MILESTONE_FAIL("重新取得里程資訊失敗"),

    /** 請選擇[%s]階段的專案 */
    SELECT_STAGE_PROJ("請選擇[%s]階段的專案"),

    /** 關閉失敗 */
    CLOSE_FAIL("關閉失敗"),

    /** (預計)專案開始日期為空 */
    PRE_PROJ_START_DATE_IS_NULL("(預計)專案開始日期為空"),

    /** (預計)專案結束日期為空 */
    PRE_PROJ_END_DATE_IS_NULL("(預計)專案結束日期為空"),

    /** 里程資訊為空 */
    EXE_MILESTONE_IS_NULL("里程資訊為空"),

    /** 送出通知失敗 */
    SEND_MAIL_FAIL("送出通知失敗"),

    /** 廠商評鑑為空 */
    EVAL_SUPPLIER_LIST_IS_NULL("此專案無廠商評鑑"),

    /** 取得廠商評鑑失敗 */
    GET_EVAL_SUPPLIER_FAIL("取得廠商評鑑失敗"),

    /** 同一人重複指派相同角色 */
    MEMBER_DUPLICATE("%s 重複指派相同角色"),

    /**
     * 必選未選
     */
    SELECT_NULL("請選擇%s"),

    /** 專案狀態變更失敗：不符合從 [%s] 變更為 [%s] 的檢核條件，錯誤原因為：%s */
    PROJ_STATUS_NON_CHANGE_ARG3("專案狀態變更失敗，不符合從 [%s] 變更為 [%s] 的檢核條件， \n錯誤原因為：\n%s "),

    /** 專案狀態變更失敗：從 %s 變更為 %s */
    PROJ_STATUS_CHANGE_FAIL_ARG2("專案狀態變更失敗，從 %s 變更為 %s "),

    /** %s必須晚於%s */
    STARTDATE_AFTER_ENDDATE_ARG2("%s必須晚於%s"),

    /** %s必須大於%s */
    IS_BIGGER_THAN_ARG2("%s必須大於%s"),

    /** [%s結束日]晚於[專案結束日]，必須先填寫變更申請單異動[專案結束日]才能設定[%s結束日] */
    ENDDATE_AFTER_PROJ_END("[%s結束日]晚於[專案結束日]，必須先填寫變更申請單異動[專案結束日]才能設定[%s結束日]"),

    /** 同專案已有送審中的[%s]，不可送審 */
    HAS_IN_PROCESS_FORM("同專案已有送審中的[%s]，不可送審"),

    /** 無專案權限 */
    TAB_NO_AUTH("無專案權限"),

    /** 頁籤轉換失敗 */
    TAB_CHANGE_FAIL("頁籤轉換失敗"),

    /** 值不存在 */
    KEY_NOT_EXIST_ARG1("欄位: %s不存在"),

    /** 匯入失敗 */
    IMPORT_FAIL("匯入失敗"),

    /** 頁面更新失敗 */
    RELOAD_FAIL("更新資料發生錯誤"),

    /** 上傳的projcode與系統中的projcode不同 */
    PROJ_CODE_DIFF_ARG3("技術支援需求單：[%s]的ICT專案代號:[%s] 與上傳的ICT專案代號:[%s] 不同"),

    /** 只能查詢12週內的資訊 */
    ONLY_QUERY_TWELVE_WEEK("只能查詢12週內的資訊"),

    /** 批次執行失敗. */
    BATCH_EXEC_FAIL("批次執行失敗"),

    /** 您目前並非此專案的[%s]，不可送審 */
    NOT_THIS_PROJ_MEMBER("您目前並非此專案的[%s]，不可送審"),

    /** 此專案沒有指派中的[%s]，不可送審 */
    THIS_PROJ_HAS_NO_MEMBER("此專案沒有指派中的[%s]，不可送審"),

    /** 發佈失敗 */
    ENABLED_FAIL("發佈失敗"),

    /** 停用失敗 */
    DISABLED_FAIL("停用失敗"),

    /** %d個面向皆須填寫：%s */
    EVAL_ASPECT_TYPE_AT_LEAST_ONE("%d個面向皆須填寫：%s"),

    /** 評估項目不可為空 */
    EVAL_ITEM_IS_EMPTY("評估項目不可為空"),

    /** 此評估面向已失效：%s */
    EVAL_ASPECT_TYPE_DISABLED("此評估面向已失效：%s"),

    /** 專案資訊變更失敗 */
    PROJ_INFO_CHANGE_FAIL("專案資訊變更失敗"),

    /** Project Code已存在PMP，但技術需求支援單號不同 */
    PROJCODE_EXIST_ICTNO_DIFF_ARG1("Project Code：[%s] 已存在PMP，但技術需求支援單號不同"),

    /** XX天XXX專案已結束不可填寫 */
    DATE_PROJECT_IS_FINISH_ARG2("%s [%s]專案已結束不可填寫"),

    /** 寫入AuditLog失敗 */
    AUDIT_LOG_FAIL("寫入AuditLog失敗"),

    /** 系統檔無ESP連結. */
    NO_ESP_URL("系統檔無ESP連結"),

    /** 合約總金額不得為0 */
    CONTRACT_AMT_ZERO("合約金額需大於0"),

    /** 格式非字串. */
    CELL_NOT_STRING("欄位: %s格式有誤，必須為文字"),

    /** 維運日期或保固日期擇一必填 */
    MAINT_WTY_DATE_REQUIRE_ONE("需要維運，維運日期或保固日期擇一必填"),

    /** 欄位: %s格式有誤，必須有@符號且前後有值 */
    NEED_SYMBOL("欄位: %s格式有誤，必須有@符號且前後有值"),

    /** 若專案狀態非"備標完成"，則匯入失敗 */
    PROJECT_NOT_IN_BID_COMPLETE_ARG2("ICT專案代號:[%s]，目前專案狀態為[%s]，必須為[備標完成]才可上傳成案資訊"),

    /** %s的%s必須晚於%s */
    STARTDATE_AFTER_ENDDATE_ARG3("%s的%s必須晚於%s"),

    ;

    private String value;

    ErrorMessageEnum(String value) {
        this.value = value;
    }

    @Override
    public String getTite() {
        return "錯誤訊息";
    }

    @Override
    public String getValue() {
        return value;
    }

}

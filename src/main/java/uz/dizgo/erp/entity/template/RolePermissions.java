package uz.dizgo.erp.entity.template;

import uz.dizgo.erp.enums.Permissions;

import java.util.Arrays;
import java.util.List;

import static uz.dizgo.erp.enums.Permissions.*;
import static uz.dizgo.erp.enums.Permissions.DELETE_LOSS;

public class RolePermissions {
    public static final List<Permissions> SUPER_ADMIN_PERMISSIONS = Arrays.stream(Permissions.values()).toList();
    public static final List<Permissions> ADMIN_PERMISSIONS = Arrays.asList(
            GET_TARIFF,

            ADD_ADDRESS,
            EDIT_ADDRESS,
            VIEW_ADDRESS,
            DELETE_ADDRESS,

            UPLOAD_MEDIA,
            DOWNLOAD_MEDIA,
            VIEW_MEDIA_INFO,
            DELETE_MEDIA,

            ADD_BRANCH,
            EDIT_BRANCH,
            VIEW_BRANCH_ADMIN,
            VIEW_BRANCH,
            DELETE_BRANCH,

            ADD_BRAND,
            EDIT_BRAND,
            VIEW_BRAND,
            DELETE_BRAND,

            ADD_CATEGORY,
            EDIT_CATEGORY,
            VIEW_CATEGORY,
            VIEW_CATEGORY_ADMIN,
            DELETE_CATEGORY,
            ADD_CHILD_CATEGORY,

            ADD_CURRENCY,
            EDIT_CURRENCY,
            VIEW_CURRENCY,
            DELETE_CURRENCY,

            ADD_CUSTOMER,
            EDIT_CUSTOMER,
            VIEW_CUSTOMER,
            VIEW_CUSTOMER_ADMIN,
            DELETE_CUSTOMER,

            ADD_MEASUREMENT,
            EDIT_MEASUREMENT,
            VIEW_MEASUREMENT,
            VIEW_MEASUREMENT_ADMIN,
            DELETE_MEASUREMENT,

            ADD_OUTLAY,
            EDIT_OUTLAY,
            VIEW_OUTLAY,
            VIEW_OUTLAY_ADMIN,
            DELETE_OUTLAY,

            ADD_PRODUCT,
            EDIT_PRODUCT,
            VIEW_PRODUCT,

            VIEW_PRODUCT_ADMIN,
            DELETE_PRODUCT,

            ADD_ROLE,
            EDIT_ROLE,
            VIEW_ROLE,
            VIEW_ROLE_ADMIN,
            DELETE_ROLE,

            ADD_SUPPLIER,
            EDIT_SUPPLIER,
            VIEW_SUPPLIER,
            VIEW_SUPPLIER_ADMIN,
            DELETE_SUPPLIER,

            ADD_USER,
            EDIT_USER,
            VIEW_USER,
            VIEW_USER_ADMIN,
            DELETE_USER,
            EDIT_MY_PROFILE,

            ADD_TRADE,
            EDIT_TRADE,
            VIEW_TRADE,
            VIEW_TRADE_ADMIN,
            DELETE_TRADE,
            DELETE_MY_TRADE,
            VIEW_MY_TRADE,

            ADD_PAY_METHOD,
            EDIT_PAY_METHOD,
            VIEW_PAY_METHOD,
            VIEW_PAY_METHOD_ADMIN,
            DELETE_PAY_METHOD,

            ADD_PAY_STATUS,
            EDIT_PAY_STATUS,
            VIEW_PAY_STATUS,
            VIEW_PAY_STATUS_ADMIN,
            DELETE_PAY_STATUS,

            ADD_PURCHASE,
            EDIT_PURCHASE,
            VIEW_PURCHASE,
            VIEW_PURCHASE_ADMIN,
            DELETE_PURCHASE,

            ADD_EXCHANGE,
            EDIT_EXCHANGE,
            VIEW_EXCHANGE,
            VIEW_EXCHANGE_ADMIN,
            DELETE_EXCHANGE,

            VIEW_BENEFIT_AND_LOST,

            ADD_CUSTOMER_GROUP,
            DELETE_CUSTOMER_GROUP,
            EDIT_CUSTOMER_GROUP,
            VIEW_CUSTOMER_GROUP,

            ADD_TAX,
            DELETE_TAX,
            EDIT_TAX,
            VIEW_TAX,
            ADD_PRODUCT_TYPE,
            GET_PRODUCT_TYPE,

            UPDATE_PRODUCT_TYPE,
            DELETE_PRODUCT_TYPE,

            GET_EXCEL,
            POST_EXCEL,

            VIEW_INFO,
            VIEW_INFO_ADMIN,


            CREATE_CONTENT,
            EDIT_CONTENT,
            GET_CONTENT,
            DELETE_CONTENT,

            CREATE_PRODUCTION,
            GET_PRODUCTION,
            VIEW_REPORT,

            GET_COURSE,


            ADD_PROJECT_TYPE,
            EDIT_PROJECT_TYPE,
            GET_PROJECT_TYPE,
            DELETE_PROJECT_TYPE,

            DELETE_TASK_STATUS,
            GET_TASK_STATUS,
            EDIT_TASK_STATUS,
            ADD_TASK_STATUS,

            DELETE_TASK_TYPE,
            GET_TASK_TYPE,
            EDIT_TASK_TYPE,
            ADD_TASK_TYPE,

            CREATE_SALARY,
            EDIT_SALARY,
            GET_SALARY,
            DELETE_SALARY,

            ADD_STAGE,
            EDIT_STAGE,
            GET_STAGE,
            DELETE_STAGE,

            DELETE_PROJECT,
            GET_PROJECT,
            EDIT_PROJECT,
            ADD_PROJECT,

            DELETE_BONUS,
            GET_BONUS,
            EDIT_BONUS,
            ADD_BONUS,

            DELETE_TASK,
            GET_TASK,
            EDIT_TASK,
            ADD_TASK,
            GET_OWN_TASK,

            ADD_PRIZE,
            VIEW_PRIZE,

            ADD_LESSON,
            VIEW_LESSON,
            VIEW_LESSON_ROLE,
            EDIT_LESSON,
            DELETE_LESSON,

            VIEW_INVOICE,
            EDIT_INVOICE,

            VIEW_LID,
            EDIT_LID,
            DELETE_LID,

            VIEW_FORM_LID,
            ADD_FORM_LID,
            EDIT_FORM_LID,
            DELETE_FORM_LID,

            VIEW_LID_STATUS,
            ADD_LID_STATUS,
            EDIT_LID_STATUS,
            DELETE_LID_STATUS,

            DELETE_JOB,
            EDIT_JOB,
            ADD_JOB,
            VIEW_JOB,

            DELETE_PROJECT_STATUS,
            GET_PROJECT_STATUS,
            EDIT_PROJECT_STATUS,
            ADD_PROJECT_STATUS,

            VIEW_ORG,
            ADD_WORK_TIME,
            GET_WORK_TIME,

            VIEW_BALANCE_HISTORY,
            VIEW_BALANCE,
            EDIT_BALANCE,
            ADD_BALANCE,

            VIEW_DASHBOARD,

            VIEW_NAVIGATION,
            DELETE_NAVIGATION,
            ADD_NAVIGATION,

            EDIT_MY_BUSINESS,
            VIEW_MY_BUSINESS,

            ADD_LOSS,
            GET_LOSS,
            EDIT_LOSS,
            DELETE_LOSS
    );
    public static final List<Permissions> MANAGER_PERMISSIONS = Arrays.asList(
            GET_TARIFF,

            ADD_ADDRESS,
            EDIT_ADDRESS,
            VIEW_ADDRESS,
            DELETE_ADDRESS,

            UPLOAD_MEDIA,
            DOWNLOAD_MEDIA,
            VIEW_MEDIA_INFO,
            DELETE_MEDIA,

            ADD_BRAND,
            EDIT_BRAND,
            VIEW_BRAND,
            DELETE_BRAND,

            ADD_CATEGORY,
            EDIT_CATEGORY,
            VIEW_CATEGORY,
            DELETE_CATEGORY,
            ADD_CHILD_CATEGORY,

            ADD_CURRENCY,
            EDIT_CURRENCY,
            VIEW_CURRENCY,
            DELETE_CURRENCY,

            ADD_CUSTOMER,
            EDIT_CUSTOMER,
            VIEW_CUSTOMER,
            DELETE_CUSTOMER,

            ADD_MEASUREMENT,
            EDIT_MEASUREMENT,
            VIEW_MEASUREMENT,
            DELETE_MEASUREMENT,

            ADD_OUTLAY,
            EDIT_OUTLAY,
            VIEW_OUTLAY,
            DELETE_OUTLAY,

            ADD_PRODUCT,
            EDIT_PRODUCT,
            VIEW_PRODUCT,
            DELETE_PRODUCT,
            VIEW_PRODUCT_ADMIN,

            ADD_ROLE,
            EDIT_ROLE,
            VIEW_ROLE,
            DELETE_ROLE,

            ADD_SUPPLIER,
            EDIT_SUPPLIER,
            VIEW_SUPPLIER,
            DELETE_SUPPLIER,

            ADD_USER,
            EDIT_USER,
            VIEW_USER,
            DELETE_USER,
            EDIT_MY_PROFILE,

            ADD_TRADE,
            EDIT_TRADE,
            VIEW_TRADE,
            DELETE_TRADE,
            DELETE_MY_TRADE,
            VIEW_MY_TRADE,

            ADD_TAX,
            DELETE_TAX,
            EDIT_TAX,
            VIEW_TAX,

            ADD_CUSTOMER_GROUP,
            DELETE_CUSTOMER_GROUP,
            EDIT_CUSTOMER_GROUP,
            VIEW_CUSTOMER_GROUP,

            ADD_PAY_METHOD,
            EDIT_PAY_METHOD,
            VIEW_PAY_METHOD,
            DELETE_PAY_METHOD,

            ADD_PAY_STATUS,
            EDIT_PAY_STATUS,
            VIEW_PAY_STATUS,
            DELETE_PAY_STATUS,

            ADD_PURCHASE,
            EDIT_PURCHASE,
            VIEW_PURCHASE,
            DELETE_PURCHASE,

            ADD_EXCHANGE,
            EDIT_EXCHANGE,
            VIEW_EXCHANGE,
            DELETE_EXCHANGE,

            VIEW_BENEFIT_AND_LOST,

            ADD_PRODUCT_TYPE,
            GET_PRODUCT_TYPE,
            UPDATE_PRODUCT_TYPE,
            DELETE_PRODUCT_TYPE,

            GET_EXCEL,
            POST_EXCEL,

            VIEW_INFO,
            VIEW_INFO_ADMIN,

            GET_BUSINESS_ALL_AMOUNT,


            CREATE_CONTENT,
            EDIT_CONTENT,
            GET_CONTENT,
            DELETE_CONTENT,

            CREATE_PRODUCTION,
            GET_PRODUCTION,
            VIEW_REPORT,

            ADD_PROJECT_TYPE,
            EDIT_PROJECT_TYPE,
            GET_PROJECT_TYPE,
            DELETE_PROJECT_TYPE,

            DELETE_TASK_STATUS,
            GET_TASK_STATUS,
            EDIT_TASK_STATUS,
            ADD_TASK_STATUS,

            DELETE_TASK_TYPE,
            GET_TASK_TYPE,
            EDIT_TASK_TYPE,
            ADD_TASK_TYPE,

            ADD_STAGE,
            EDIT_STAGE,
            GET_STAGE,
            DELETE_STAGE,

            DELETE_PROJECT,
            GET_PROJECT,
            EDIT_PROJECT,
            ADD_PROJECT,

            DELETE_BONUS,
            GET_BONUS,
            EDIT_BONUS,
            ADD_BONUS,

            ADD_PRIZE,
            VIEW_PRIZE,

            ADD_LESSON,
            VIEW_LESSON,
            VIEW_LESSON_ROLE,
            EDIT_LESSON,
            DELETE_LESSON,

            VIEW_INVOICE,
            EDIT_INVOICE,

            VIEW_LID,
            EDIT_LID,
            DELETE_LID,

            VIEW_FORM_LID,
            ADD_FORM_LID,
            EDIT_FORM_LID,
            DELETE_FORM_LID,

            VIEW_LID_STATUS,
            ADD_LID_STATUS,
            EDIT_LID_STATUS,
            DELETE_LID_STATUS,

            DELETE_JOB,
            EDIT_JOB,
            ADD_JOB,
            VIEW_JOB,

            DELETE_PROJECT_STATUS,
            GET_PROJECT_STATUS,
            EDIT_PROJECT_STATUS,
            ADD_PROJECT_STATUS,
            ADD_WORK_TIME,
            GET_WORK_TIME,

            GET_OWN_TASK,

            VIEW_DASHBOARD,

            VIEW_NAVIGATION,
            DELETE_NAVIGATION,
            ADD_NAVIGATION
    );
    public static final List<Permissions> EMPLOYEE_PERMISSIONS = Arrays.asList(
            DOWNLOAD_MEDIA,
            VIEW_MEDIA_INFO,
            VIEW_BRAND,
            ADD_CURRENCY,
            EDIT_CURRENCY,
            VIEW_CURRENCY,
            DELETE_CURRENCY,
            ADD_MEASUREMENT,
            EDIT_MEASUREMENT,
            VIEW_MEASUREMENT,
            DELETE_MEASUREMENT,

            ADD_TRADE,
            EDIT_TRADE,
            VIEW_MY_TRADE,
            DELETE_MY_TRADE,

            ADD_PAY_METHOD,
            EDIT_PAY_METHOD,
            VIEW_PAY_METHOD,
            DELETE_PAY_METHOD,
            ADD_PAY_STATUS,
            EDIT_PAY_STATUS,
            VIEW_PAY_STATUS,
            DELETE_PAY_STATUS,
            EDIT_MY_PROFILE,
            VIEW_PRODUCT,

            CREATE_CONTENT,
            EDIT_CONTENT,
            GET_CONTENT,
            DELETE_CONTENT,

            CREATE_PRODUCTION,
            GET_PRODUCTION,

            GET_WORK_TIME,

            VIEW_DASHBOARD
    );

    public static final List<Permissions> CUSTOMER_PERMISSIONS = Arrays.asList(
            VIEW_TRADE,
            EDIT_CUSTOMER
    );

    public static final List<Permissions> CASHIER_PERMISSIONS = Arrays.asList(
            VIEW_PRODUCT,
            VIEW_MEASUREMENT,
            VIEW_CATEGORY,
            VIEW_BRANCH,
            VIEW_BRAND,
            ADD_CUSTOMER,
            VIEW_CUSTOMER,
            ADD_TRADE,
            VIEW_TRADE,
            VIEW_PAY_METHOD,
            VIEW_PURCHASE
    );

    public static final List<Permissions> MANAGER_PERMISSIONS_FOR_OTHERS = Arrays.asList(
            UPLOAD_MEDIA,
            DOWNLOAD_MEDIA,
            VIEW_MEDIA_INFO,
            DELETE_MEDIA,

            ADD_BRAND,
            EDIT_BRAND,
            VIEW_BRAND,
            DELETE_BRAND,

            ADD_CATEGORY,
            EDIT_CATEGORY,
            VIEW_CATEGORY,
            DELETE_CATEGORY,
            ADD_CHILD_CATEGORY,

            ADD_CUSTOMER,
            EDIT_CUSTOMER,
            VIEW_CUSTOMER,
            DELETE_CUSTOMER,

            ADD_MEASUREMENT,
            EDIT_MEASUREMENT,
            VIEW_MEASUREMENT,
            DELETE_MEASUREMENT,

            ADD_OUTLAY,
            EDIT_OUTLAY,
            VIEW_OUTLAY,
            DELETE_OUTLAY,

            ADD_PRODUCT,
            EDIT_PRODUCT,
            VIEW_PRODUCT,
            DELETE_PRODUCT,
            VIEW_PRODUCT_ADMIN,

            ADD_SUPPLIER,
            EDIT_SUPPLIER,
            VIEW_SUPPLIER,
            DELETE_SUPPLIER,

            ADD_TRADE,
            EDIT_TRADE,
            VIEW_TRADE,
            DELETE_TRADE,
            DELETE_MY_TRADE,
            VIEW_MY_TRADE,

            ADD_TAX,
            DELETE_TAX,
            EDIT_TAX,
            VIEW_TAX,

            ADD_CUSTOMER_GROUP,
            DELETE_CUSTOMER_GROUP,
            EDIT_CUSTOMER_GROUP,
            VIEW_CUSTOMER_GROUP,


            ADD_PAY_STATUS,
            EDIT_PAY_STATUS,
            VIEW_PAY_STATUS,
            DELETE_PAY_STATUS,

            ADD_PURCHASE,
            EDIT_PURCHASE,
            VIEW_PURCHASE,
            DELETE_PURCHASE,

            ADD_EXCHANGE,
            EDIT_EXCHANGE,
            VIEW_EXCHANGE,
            DELETE_EXCHANGE,

            VIEW_BENEFIT_AND_LOST,

            GET_EXCEL,
            POST_EXCEL,

            CREATE_CONTENT,
            EDIT_CONTENT,
            GET_CONTENT,
            DELETE_CONTENT,

            CREATE_PRODUCTION,
            GET_PRODUCTION,
            VIEW_REPORT,

            ADD_PROJECT_TYPE,
            EDIT_PROJECT_TYPE,
            GET_PROJECT_TYPE,
            DELETE_PROJECT_TYPE,

            DELETE_TASK_STATUS,
            GET_TASK_STATUS,
            EDIT_TASK_STATUS,
            ADD_TASK_STATUS,

            DELETE_TASK_TYPE,
            GET_TASK_TYPE,
            EDIT_TASK_TYPE,
            ADD_TASK_TYPE,

            ADD_STAGE,
            EDIT_STAGE,
            GET_STAGE,
            DELETE_STAGE,

            DELETE_PROJECT,
            GET_PROJECT,
            EDIT_PROJECT,
            ADD_PROJECT,

            DELETE_BONUS,
            GET_BONUS,
            EDIT_BONUS,
            ADD_BONUS,

            ADD_PRIZE,
            VIEW_PRIZE,

            ADD_LESSON,
            VIEW_LESSON,
            VIEW_LESSON_ROLE,
            EDIT_LESSON,
            DELETE_LESSON,

            VIEW_INVOICE,
            EDIT_INVOICE,

            VIEW_LID,
            EDIT_LID,
            DELETE_LID,

            VIEW_FORM_LID,
            ADD_FORM_LID,
            EDIT_FORM_LID,
            DELETE_FORM_LID,

            VIEW_LID_STATUS,
            ADD_LID_STATUS,
            EDIT_LID_STATUS,
            DELETE_LID_STATUS,

            DELETE_JOB,
            EDIT_JOB,
            ADD_JOB,
            VIEW_JOB,

            DELETE_PROJECT_STATUS,
            GET_PROJECT_STATUS,
            EDIT_PROJECT_STATUS,
            ADD_PROJECT_STATUS,

            ADD_WORK_TIME,
            GET_WORK_TIME,

            GET_OWN_TASK,

            VIEW_DASHBOARD
    );
}

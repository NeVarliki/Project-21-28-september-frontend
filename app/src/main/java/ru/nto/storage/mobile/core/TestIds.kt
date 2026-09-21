package ru.nto.storage.mobile.core

object TestIds {
    object Auth {
        const val ERROR = "auth_error"
        const val SIGN_BUTTON = "auth_sign_button"
        const val CODE_INPUT = "auth_code_input"
    }
    object Main {
        const val ERROR = "main_error"
        const val EMPTY = "main_empty"
        const val ADD_BUTTON = "main_add_button"
        const val REFRESH_BUTTON = "main_refresh_button"
        const val LOGOUT_BUTTON = "main_logout_button"
        const val PROFILE_IMAGE = "main_image"
        const val PROFILE_NAME = "main_name"
        const val PROFILE_DEPARTMENT = "main_department"
        const val ITEM_NAME = "main_item_name"
        const val ITEM_CODE = "main_item_code"
        const val ITEM_DATE = "main_item_date"

        fun getIdItemByPosition(position: Int) = "main_issue_pos_$position"
    }

    object Take {
        const val ERROR = "take_error"
        const val EMPTY = "take_empty"
        const val REFRESH_BUTTON = "take_refresh_button"
        const val BACK_BUTTON = "take_back_button"
        const val TAKE_BUTTON = "take_take_button"
        const val DATE_INPUT = "take_date_input"
        const val ITEM_CATEGORY = "take_category"
        const val ITEM_EQUIPMENT_TEXT = "take_equipment_text"
        const val ITEM_EQUIPMENT_SELECTOR = "take_equipment_selector"

        fun getIdCategoryItemByPosition(position: Int) = "take_category_pos_$position"
        fun getIdEquipmentItemByPosition(position: Int) = "take_equipment_pos_$position"
    }
}

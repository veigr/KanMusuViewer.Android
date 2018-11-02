package net.cat_ears.kanmusuviewer.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

data class Start2(
//    @Json(name = "api_result") val apiResult: Int,
//    @Json(name = "api_result_msg") val apiResultMsg: String,
        @Json(name = "api_data") val apiData: ApiData
) {
    companion object {
        val empty = Start2(
                ApiData(
                        listOf(ApiMstShip(0, "", "")),
                        listOf(ApiMstShipgraph(
                                0,
                                "",
                                listOf("")
                        ))
                )
        )

        fun parse(text: String): List<Ship>? {
            val json = text.removePrefix("svdata=")
            val moshi = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            try {
                val start2 = moshi.adapter(Start2::class.java).fromJson(json) ?: return null

                return start2.apiData.apiMstShip
                        .map { s -> Ship.parse(s, start2.apiData.apiMstShipgraph.find { g -> s.apiId == g.apiId }) }
            } catch (e: JsonEncodingException) {
                return null
            }
        }
    }
}


data class ApiData(
        @Json(name = "api_mst_ship") val apiMstShip: List<ApiMstShip>,
//    @Json(name = "api_mst_slotitem_equiptype") val apiMstSlotitemEquiptype: List<ApiMstSlotitemEquiptype>,
//    @Json(name = "api_mst_equip_exslot") val apiMstEquipExslot: List<Int>,
//    @Json(name = "api_mst_equip_exslot_ship") val apiMstEquipExslotShip: List<ApiMstEquipExslotShip>,
//    @Json(name = "api_mst_stype") val apiMstStype: List<ApiMstStype>,
//    @Json(name = "api_mst_slotitem") val apiMstSlotitem: List<ApiMstSlotitem>,
//    @Json(name = "api_mst_furnituregraph") val apiMstFurnituregraph: List<ApiMstFurnituregraph>,
//    @Json(name = "api_mst_useitem") val apiMstUseitem: List<ApiMstUseitem>,
//    @Json(name = "api_mst_payitem") val apiMstPayitem: List<ApiMstPayitem>,
//    @Json(name = "api_mst_item_shop") val apiMstItemShop: ApiMstItemShop,
//    @Json(name = "api_mst_maparea") val apiMstMaparea: List<ApiMstMaparea>,
//    @Json(name = "api_mst_mapinfo") val apiMstMapinfo: List<ApiMstMapinfo>,
//    @Json(name = "api_mst_mapbgm") val apiMstMapbgm: List<ApiMstMapbgm>,
//    @Json(name = "api_mst_mission") val apiMstMission: List<ApiMstMission>,
//    @Json(name = "api_mst_const") val apiMstConst: ApiMstConst,
//    @Json(name = "api_mst_shipupgrade") val apiMstShipupgrade: List<ApiMstShipupgrade>,
//    @Json(name = "api_mst_bgm") val apiMstBgm: List<ApiMstBgm>,
//    @Json(name = "api_mst_equip_ship") val apiMstEquipShip: List<ApiMstEquipShip>,
//    @Json(name = "api_mst_furniture") val apiMstFurniture: List<ApiMstFurniture>,
        @Json(name = "api_mst_shipgraph") val apiMstShipgraph: List<ApiMstShipgraph>
)

data class ApiMstEquipShip(
        @Json(name = "api_ship_id") val apiShipId: Int,
        @Json(name = "api_equip_type") val apiEquipType: List<Int>
)

data class ApiMstUseitem(
        @Json(name = "api_id") val apiId: Int,
        @Json(name = "api_usetype") val apiUsetype: Int,
        @Json(name = "api_category") val apiCategory: Int,
        @Json(name = "api_name") val apiName: String,
        @Json(name = "api_description") val apiDescription: List<String>,
        @Json(name = "api_price") val apiPrice: Int
)

data class ApiMstShipupgrade(
        @Json(name = "api_id") val apiId: Int,
        @Json(name = "api_current_ship_id") val apiCurrentShipId: Int,
        @Json(name = "api_original_ship_id") val apiOriginalShipId: Int,
        @Json(name = "api_upgrade_type") val apiUpgradeType: Int,
        @Json(name = "api_upgrade_level") val apiUpgradeLevel: Int,
        @Json(name = "api_drawing_count") val apiDrawingCount: Int,
        @Json(name = "api_catapult_count") val apiCatapultCount: Int,
        @Json(name = "api_report_count") val apiReportCount: Int,
        @Json(name = "api_sortno") val apiSortno: Int
)

data class ApiMstEquipExslotShip(
        @Json(name = "api_slotitem_id") val apiSlotitemId: Int,
        @Json(name = "api_ship_ids") val apiShipIds: List<Int>
)

data class ApiMstMapinfo(
        @Json(name = "api_id") val apiId: Int,
        @Json(name = "api_maparea_id") val apiMapareaId: Int,
        @Json(name = "api_no") val apiNo: Int,
        @Json(name = "api_name") val apiName: String,
        @Json(name = "api_level") val apiLevel: Int,
        @Json(name = "api_opetext") val apiOpetext: String,
        @Json(name = "api_infotext") val apiInfotext: String,
        @Json(name = "api_item") val apiItem: List<Int>,
        @Json(name = "api_max_maphp") val apiMaxMaphp: Any?,
        @Json(name = "api_required_defeat_count") val apiRequiredDefeatCount: Int,
        @Json(name = "api_sally_flag") val apiSallyFlag: List<Int>
)

data class ApiMstShipgraph(
        @Json(name = "api_id") val apiId: Int,
//    @Json(name = "api_sortno") val apiSortno: Int,
        @Json(name = "api_filename") val apiFilename: String,
        @Json(name = "api_version") val apiVersion: List<String>
//    @Json(name = "api_boko_n") val apiBokoN: List<Int>,
//    @Json(name = "api_boko_d") val apiBokoD: List<Int>,
//    @Json(name = "api_kaisyu_n") val apiKaisyuN: List<Int>,
//    @Json(name = "api_kaisyu_d") val apiKaisyuD: List<Int>,
//    @Json(name = "api_kaizo_n") val apiKaizoN: List<Int>,
//    @Json(name = "api_kaizo_d") val apiKaizoD: List<Int>,
//    @Json(name = "api_map_n") val apiMapN: List<Int>,
//    @Json(name = "api_map_d") val apiMapD: List<Int>,
//    @Json(name = "api_ensyuf_n") val apiEnsyufN: List<Int>,
//    @Json(name = "api_ensyuf_d") val apiEnsyufD: List<Int>,
//    @Json(name = "api_ensyue_n") val apiEnsyueN: List<Int>,
//    @Json(name = "api_battle_n") val apiBattleN: List<Int>,
//    @Json(name = "api_battle_d") val apiBattleD: List<Int>,
//    @Json(name = "api_weda") val apiWeda: List<Int>,
//    @Json(name = "api_wedb") val apiWedb: List<Int>
)

data class ApiMstSlotitem(
        @Json(name = "api_id") val apiId: Int,
        @Json(name = "api_sortno") val apiSortno: Int,
        @Json(name = "api_name") val apiName: String,
        @Json(name = "api_type") val apiType: List<Int>,
        @Json(name = "api_taik") val apiTaik: Int,
        @Json(name = "api_souk") val apiSouk: Int,
        @Json(name = "api_houg") val apiHoug: Int,
        @Json(name = "api_raig") val apiRaig: Int,
        @Json(name = "api_soku") val apiSoku: Int,
        @Json(name = "api_baku") val apiBaku: Int,
        @Json(name = "api_tyku") val apiTyku: Int,
        @Json(name = "api_tais") val apiTais: Int,
        @Json(name = "api_atap") val apiAtap: Int,
        @Json(name = "api_houm") val apiHoum: Int,
        @Json(name = "api_raim") val apiRaim: Int,
        @Json(name = "api_houk") val apiHouk: Int,
        @Json(name = "api_raik") val apiRaik: Int,
        @Json(name = "api_bakk") val apiBakk: Int,
        @Json(name = "api_saku") val apiSaku: Int,
        @Json(name = "api_sakb") val apiSakb: Int,
        @Json(name = "api_luck") val apiLuck: Int,
        @Json(name = "api_leng") val apiLeng: Int,
        @Json(name = "api_rare") val apiRare: Int,
        @Json(name = "api_broken") val apiBroken: List<Int>,
        @Json(name = "api_info") val apiInfo: String,
        @Json(name = "api_usebull") val apiUsebull: String,
        @Json(name = "api_version") val apiVersion: Int,
        @Json(name = "api_cost") val apiCost: Int,
        @Json(name = "api_distance") val apiDistance: Int
)

data class ApiMstItemShop(
        @Json(name = "api_cabinet_1") val apiCabinet1: List<Int>,
        @Json(name = "api_cabinet_2") val apiCabinet2: List<Int>
)

data class ApiMstConst(
        @Json(name = "api_boko_max_ships") val apiBokoMaxShips: ApiBokoMaxShips,
        @Json(name = "api_parallel_quest_max") val apiParallelQuestMax: ApiParallelQuestMax,
        @Json(name = "api_dpflag_quest") val apiDpflagQuest: ApiDpflagQuest
)

data class ApiDpflagQuest(
        @Json(name = "api_string_value") val apiStringValue: String,
        @Json(name = "api_int_value") val apiIntValue: Int
)

data class ApiParallelQuestMax(
        @Json(name = "api_string_value") val apiStringValue: String,
        @Json(name = "api_int_value") val apiIntValue: Int
)

data class ApiBokoMaxShips(
        @Json(name = "api_string_value") val apiStringValue: String,
        @Json(name = "api_int_value") val apiIntValue: Int
)

data class ApiMstFurnituregraph(
        @Json(name = "api_id") val apiId: Int,
        @Json(name = "api_type") val apiType: Int,
        @Json(name = "api_no") val apiNo: Int,
        @Json(name = "api_filename") val apiFilename: String,
        @Json(name = "api_version") val apiVersion: String
)

data class ApiMstBgm(
        @Json(name = "api_id") val apiId: Int,
        @Json(name = "api_name") val apiName: String
)

data class ApiMstFurniture(
        @Json(name = "api_id") val apiId: Int,
        @Json(name = "api_type") val apiType: Int,
        @Json(name = "api_no") val apiNo: Int,
        @Json(name = "api_title") val apiTitle: String,
        @Json(name = "api_description") val apiDescription: String,
        @Json(name = "api_rarity") val apiRarity: Int,
        @Json(name = "api_price") val apiPrice: Int,
        @Json(name = "api_saleflg") val apiSaleflg: Int,
        @Json(name = "api_season") val apiSeason: Int,
        @Json(name = "api_version") val apiVersion: Int,
        @Json(name = "api_outside_id") val apiOutsideId: Int,
        @Json(name = "api_active_flag") val apiActiveFlag: Int
)

data class ApiMstShip(
        @Json(name = "api_id") val apiId: Int,
//    @Json(name = "api_sortno") val apiSortno: Int,
//    @Json(name = "api_sort_id") val apiSortId: Int,
        @Json(name = "api_name") val apiName: String,
        @Json(name = "api_yomi") val apiYomi: String
//    @Json(name = "api_stype") val apiStype: Int,
//    @Json(name = "api_ctype") val apiCtype: Int,
//    @Json(name = "api_afterlv") val apiAfterlv: Int,
//    @Json(name = "api_aftershipid") val apiAftershipid: String,
//    @Json(name = "api_taik") val apiTaik: List<Int>,
//    @Json(name = "api_souk") val apiSouk: List<Int>,
//    @Json(name = "api_houg") val apiHoug: List<Int>,
//    @Json(name = "api_raig") val apiRaig: List<Int>,
//    @Json(name = "api_tyku") val apiTyku: List<Int>,
//    @Json(name = "api_luck") val apiLuck: List<Int>,
//    @Json(name = "api_soku") val apiSoku: Int,
//    @Json(name = "api_leng") val apiLeng: Int,
//    @Json(name = "api_slot_num") val apiSlotNum: Int,
//    @Json(name = "api_maxeq") val apiMaxeq: List<Int>,
//    @Json(name = "api_buildtime") val apiBuildtime: Int,
//    @Json(name = "api_broken") val apiBroken: List<Int>,
//    @Json(name = "api_powup") val apiPowup: List<Int>,
//    @Json(name = "api_backs") val apiBacks: Int,
//    @Json(name = "api_getmes") val apiGetmes: String,
//    @Json(name = "api_afterfuel") val apiAfterfuel: Int,
//    @Json(name = "api_afterbull") val apiAfterbull: Int,
//    @Json(name = "api_fuel_max") val apiFuelMax: Int,
//    @Json(name = "api_bull_max") val apiBullMax: Int,
//    @Json(name = "api_voicef") val apiVoicef: Int,
//    @Json(name = "api_tais") val apiTais: List<Int>
)

data class ApiMstMapbgm(
        @Json(name = "api_id") val apiId: Int,
        @Json(name = "api_maparea_id") val apiMapareaId: Int,
        @Json(name = "api_no") val apiNo: Int,
        @Json(name = "api_moving_bgm") val apiMovingBgm: Int,
        @Json(name = "api_map_bgm") val apiMapBgm: List<Int>,
        @Json(name = "api_boss_bgm") val apiBossBgm: List<Int>
)

data class ApiMstSlotitemEquiptype(
        @Json(name = "api_id") val apiId: Int,
        @Json(name = "api_name") val apiName: String,
        @Json(name = "api_show_flg") val apiShowFlg: Int
)

data class ApiMstMission(
        @Json(name = "api_id") val apiId: Int,
        @Json(name = "api_disp_no") val apiDispNo: String,
        @Json(name = "api_maparea_id") val apiMapareaId: Int,
        @Json(name = "api_name") val apiName: String,
        @Json(name = "api_details") val apiDetails: String,
        @Json(name = "api_time") val apiTime: Int,
        @Json(name = "api_deck_num") val apiDeckNum: Int,
        @Json(name = "api_difficulty") val apiDifficulty: Int,
        @Json(name = "api_use_fuel") val apiUseFuel: Double,
        @Json(name = "api_use_bull") val apiUseBull: Double,
        @Json(name = "api_win_item1") val apiWinItem1: List<Int>,
        @Json(name = "api_win_item2") val apiWinItem2: List<Int>,
        @Json(name = "api_return_flag") val apiReturnFlag: Int
)

data class ApiMstStype(
        @Json(name = "api_id") val apiId: Int,
        @Json(name = "api_sortno") val apiSortno: Int,
        @Json(name = "api_name") val apiName: String,
        @Json(name = "api_scnt") val apiScnt: Int,
        @Json(name = "api_kcnt") val apiKcnt: Int,
        @Json(name = "api_equip_type") val apiEquipType: ApiEquipType
)

data class ApiEquipType(
        @Json(name = "1") val x1: Int,
        @Json(name = "2") val x2: Int,
        @Json(name = "3") val x3: Int,
        @Json(name = "4") val x4: Int,
        @Json(name = "5") val x5: Int,
        @Json(name = "6") val x6: Int,
        @Json(name = "7") val x7: Int,
        @Json(name = "8") val x8: Int,
        @Json(name = "9") val x9: Int,
        @Json(name = "10") val x10: Int,
        @Json(name = "11") val x11: Int,
        @Json(name = "12") val x12: Int,
        @Json(name = "13") val x13: Int,
        @Json(name = "14") val x14: Int,
        @Json(name = "15") val x15: Int,
        @Json(name = "16") val x16: Int,
        @Json(name = "17") val x17: Int,
        @Json(name = "18") val x18: Int,
        @Json(name = "19") val x19: Int,
        @Json(name = "20") val x20: Int,
        @Json(name = "21") val x21: Int,
        @Json(name = "22") val x22: Int,
        @Json(name = "23") val x23: Int,
        @Json(name = "24") val x24: Int,
        @Json(name = "25") val x25: Int,
        @Json(name = "26") val x26: Int,
        @Json(name = "27") val x27: Int,
        @Json(name = "28") val x28: Int,
        @Json(name = "29") val x29: Int,
        @Json(name = "30") val x30: Int,
        @Json(name = "31") val x31: Int,
        @Json(name = "32") val x32: Int,
        @Json(name = "33") val x33: Int,
        @Json(name = "34") val x34: Int,
        @Json(name = "35") val x35: Int,
        @Json(name = "36") val x36: Int,
        @Json(name = "37") val x37: Int,
        @Json(name = "38") val x38: Int,
        @Json(name = "39") val x39: Int,
        @Json(name = "40") val x40: Int,
        @Json(name = "41") val x41: Int,
        @Json(name = "42") val x42: Int,
        @Json(name = "43") val x43: Int,
        @Json(name = "44") val x44: Int,
        @Json(name = "45") val x45: Int,
        @Json(name = "46") val x46: Int,
        @Json(name = "47") val x47: Int,
        @Json(name = "48") val x48: Int,
        @Json(name = "49") val x49: Int,
        @Json(name = "50") val x50: Int,
        @Json(name = "51") val x51: Int,
        @Json(name = "52") val x52: Int,
        @Json(name = "53") val x53: Int,
        @Json(name = "54") val x54: Int,
        @Json(name = "55") val x55: Int,
        @Json(name = "56") val x56: Int,
        @Json(name = "57") val x57: Int,
        @Json(name = "58") val x58: Int,
        @Json(name = "59") val x59: Int,
        @Json(name = "60") val x60: Int,
        @Json(name = "61") val x61: Int,
        @Json(name = "62") val x62: Int,
        @Json(name = "63") val x63: Int,
        @Json(name = "64") val x64: Int,
        @Json(name = "65") val x65: Int,
        @Json(name = "66") val x66: Int,
        @Json(name = "67") val x67: Int,
        @Json(name = "68") val x68: Int,
        @Json(name = "69") val x69: Int,
        @Json(name = "70") val x70: Int,
        @Json(name = "71") val x71: Int,
        @Json(name = "72") val x72: Int,
        @Json(name = "73") val x73: Int,
        @Json(name = "74") val x74: Int,
        @Json(name = "75") val x75: Int,
        @Json(name = "76") val x76: Int,
        @Json(name = "77") val x77: Int,
        @Json(name = "78") val x78: Int,
        @Json(name = "79") val x79: Int,
        @Json(name = "80") val x80: Int,
        @Json(name = "81") val x81: Int,
        @Json(name = "82") val x82: Int,
        @Json(name = "83") val x83: Int,
        @Json(name = "84") val x84: Int,
        @Json(name = "85") val x85: Int,
        @Json(name = "86") val x86: Int,
        @Json(name = "87") val x87: Int,
        @Json(name = "88") val x88: Int,
        @Json(name = "89") val x89: Int,
        @Json(name = "90") val x90: Int,
        @Json(name = "91") val x91: Int,
        @Json(name = "92") val x92: Int,
        @Json(name = "93") val x93: Int,
        @Json(name = "94") val x94: Int
)

data class ApiMstMaparea(
        @Json(name = "api_id") val apiId: Int,
        @Json(name = "api_name") val apiName: String,
        @Json(name = "api_type") val apiType: Int
)

data class ApiMstPayitem(
        @Json(name = "api_id") val apiId: Int,
        @Json(name = "api_type") val apiType: Int,
        @Json(name = "api_name") val apiName: String,
        @Json(name = "api_description") val apiDescription: String,
        @Json(name = "api_shop_description") val apiShopDescription: String,
        @Json(name = "api_item") val apiItem: List<Int>,
        @Json(name = "api_price") val apiPrice: Int
)
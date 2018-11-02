package net.cat_ears.kanmusuviewer.model

data class Ship(
        val id: Int,
        val name: String,
        val kana: String,
        val imageVersion: Int,
        val graphFileName: String?
) {
    companion object {
        fun parse(ship: ApiMstShip, shipGraph: ApiMstShipgraph?): Ship {
            return Ship(
                    ship.apiId,
                    ship.apiName,
                    ship.apiYomi,
                    shipGraph?.apiVersion?.first()?.toInt() ?: 0,
                    shipGraph?.apiFilename)
        }
    }
}
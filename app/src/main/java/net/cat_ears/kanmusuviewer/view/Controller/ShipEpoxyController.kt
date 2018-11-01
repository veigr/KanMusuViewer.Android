package net.cat_ears.kanmusuviewer.view.Controller

import com.airbnb.epoxy.Typed2EpoxyController
import net.cat_ears.kanmusuviewer.FragmentShiplistItemBindingModel_
import net.cat_ears.kanmusuviewer.model.Ship

class ShipEpoxyController : Typed2EpoxyController<List<Ship>?, (Int) -> Unit>() {
    override fun buildModels(ships: List<Ship>?, clickHandler: (Int) -> Unit) {
        ships?.forEach { ship ->
            FragmentShiplistItemBindingModel_()
                    .id(ship.id)
                    .shipId(ship.id.toString())
                    .shipName(ship.name)
                    .onClick { _ ->
                        clickHandler(ship.id)
                    }
                    .addTo(this)
        }
    }
}
package com.aira.vision.inspection.data

data class SnagItem(
    val sno: Int,
    val description: String
)

data class SnagSection(
    val id: String,
    val title: String,
    val icon: String,
    val items: List<SnagItem>
)

data class DimensionData(
    val area: String,
    val brochure: String,
    val measured: String,
    val status: String,
    val comment: String
)

object SnagDataProvider {

    val SNAG_DATA = listOf(
        SnagSection(
            id = "bedroom-1",
            title = "BEDROOM -1",
            icon = "\uD83C\uDFE0",
            items = listOf(
                SnagItem(1, "Hollowness Observed on floor tile"),
                SnagItem(2, "Wall 2nd coat paint pending from builder side"),
                SnagItem(3, "Wall Undulation Observed"),
                SnagItem(4, "Ceiling shade variation observed"),
                SnagItem(5, "Door frame polish not done"),
                SnagItem(6, "Door shutter polish not done properly"),
                SnagItem(7, "Shutter and frame not in line"),
                SnagItem(8, "Hinges not screwed properly"),
                SnagItem(9, "Entire door fastner cap missing"),
                SnagItem(10, "Stains observed on glass Scratches"),
                SnagItem(11, "Dead mortar on windows"),
                SnagItem(12, "Faster caps missing entire window And screws were protuding sharply"),
                SnagItem(13, "Entire flat switch face plate missing")
            )
        ),
        SnagSection(
            id = "bathroom-1",
            title = "Bathroom -1",
            icon = "\uD83D\uDEBF",
            items = listOf(
                SnagItem(1, "Hollowness observed on floor tiles"),
                SnagItem(2, "Offset on floor tiles"),
                SnagItem(3, "Wall tiles Hallowness observed"),
                SnagItem(4, "Offset observedon wall tiles"),
                SnagItem(5, "Wall tile chippoff Observed"),
                SnagItem(6, "Wall tile cutouts not selead properly"),
                SnagItem(7, "Frame damage observed"),
                SnagItem(8, "Frame polishing not done properly"),
                SnagItem(9, "Frame gaps and door shutter and frame not in line"),
                SnagItem(10, "Door fastner caps missing"),
                SnagItem(11, "Window stains and fittings not fixed properly"),
                SnagItem(12, "Track not cleaned properly"),
                SnagItem(13, "Gaps observed between sanitary fittings")
            )
        ),
        SnagSection(
            id = "bedroom-2",
            title = "Bedroom-2",
            icon = "\uD83D\uDECF\uFE0F",
            items = listOf(
                SnagItem(1, "Skirting groove not in line"),
                SnagItem(2, "Scratches and hallowness observed"),
                SnagItem(3, "Shade variation observed on wall surface"),
                SnagItem(4, "Frame polish not done properly"),
                SnagItem(5, "Frame crack"),
                SnagItem(6, "Door frame and shutter not in line"),
                SnagItem(7, "Door hardware not fixed properly"),
                SnagItem(8, "Fastner caps missing"),
                SnagItem(9, "Shutter polish work not done properly"),
                SnagItem(10, "Edges finishing not done properly"),
                SnagItem(11, "Wall undulation observed"),
                SnagItem(12, "Window stains observed and scratches were found"),
                SnagItem(13, "Stains observed on window"),
                SnagItem(14, "Fastner caps missing"),
                SnagItem(15, "Sealant gaps observed"),
                SnagItem(16, "Entire flat switch plates missing")
            )
        ),
        SnagSection(
            id = "bathroom-2",
            title = "Bathroom -2",
            icon = "\uD83D\uDEBF",
            items = listOf(
                SnagItem(1, "Chippoff observedn on floor tiles"),
                SnagItem(2, "Wall tiles Hallowness observed"),
                SnagItem(3, "Offsets observed"),
                SnagItem(4, "Tile damage observed"),
                SnagItem(5, "Wall tiles cutouts."),
                SnagItem(6, "Frame polish"),
                SnagItem(7, "Rubber gasket damage"),
                SnagItem(8, "Fastner caps missing"),
                SnagItem(9, "Door shutter and frame not in line"),
                SnagItem(10, "Stains and fittings not fixed properly"),
                SnagItem(11, "Track not cleaned properly"),
                SnagItem(12, "Gaps observed between wall and sanitory fitting")
            )
        ),
        SnagSection(
            id = "bedroom-3",
            title = "Bedroom-3",
            icon = "\uD83D\uDECF\uFE0F",
            items = listOf(
                SnagItem(1, "Scratches on floor tiles"),
                SnagItem(2, "Hallowness and offset observed"),
                SnagItem(3, "Wall undulation"),
                SnagItem(4, "Shade variation observed"),
                SnagItem(5, "Frame polish"),
                SnagItem(6, "Shutter and frame not in line")
            )
        ),
        SnagSection(
            id = "bedroom-toilet-3",
            title = "Bedroom--3",
            icon = "\uD83D\uDEBF",
            items = listOf(
                SnagItem(7, "Fittings not fixed properly"),
                SnagItem(8, "Fastner caps missing"),
                SnagItem(9, "Fire exist selead in wall"),
                SnagItem(10, "Window not opening properly Mesh damage observed Stains and scratches"),
                SnagItem(11, "Fastner capsmissing")
            )
        ),
        SnagSection(
            id = "bathroom-3",
            title = "Bathroom -3",
            icon = "\uD83D\uDEBF",
            items = listOf(
                SnagItem(1, "Tile damage observed on flooor tile"),
                SnagItem(2, "2nd coat paint pending"),
                SnagItem(3, "Hallow tile"),
                SnagItem(4, "Hallowness observed"),
                SnagItem(5, "Toliet ledge wall champers off"),
                SnagItem(6, "Cutouts not selead properly"),
                SnagItem(7, "Frame polish"),
                SnagItem(8, "Shutter polish"),
                SnagItem(9, "Fatsner caps missing"),
                SnagItem(10, "Shutter and frame not in line"),
                SnagItem(11, "Dead motar and dust observed in UPVC frame"),
                SnagItem(12, "Gaps observed between wall and sanitory fitting")
            )
        ),
        SnagSection(
            id = "bedroom-4",
            title = "Bedroom-4",
            icon = "\uD83D\uDECF\uFE0F",
            items = listOf(
                SnagItem(1, "Tile damage observed on flooor tile"),
                SnagItem(2, "2nd coat paint pending"),
                SnagItem(3, "Hallow tile"),
                SnagItem(5, "Wall undulation observed"),
                SnagItem(6, "Frame polish"),
                SnagItem(7, "Shutter polish"),
                SnagItem(7, "Stains observed on windows"),
                SnagItem(8, "Frame chippoff"),
                SnagItem(9, "Gaps found between door frame and shutter"),
                SnagItem(10, "Door handles and hardware were not fixed properly"),
                SnagItem(11, "Mesh damage Stains and scrathes"),
                SnagItem(12, "Fastner Caps missing"),
                SnagItem(13, "Dead mortor and stains observed on windows")
            )
        ),
        SnagSection(
            id = "kitchen",
            title = "Kitchen",
            icon = "\uD83C\uDF73",
            items = listOf(
                SnagItem(1, "Hallowness observed on floor tiles"),
                SnagItem(2, "Scratches Observed"),
                SnagItem(3, "Chippoff Observed"),
                SnagItem(4, "Wall damage observed"),
                SnagItem(5, "Wall undulation observed"),
                SnagItem(6, "Window stains and scathes and cleaning pending")
            )
        ),
        SnagSection(
            id = "utility-area",
            title = "Utility Area",
            icon = "\uD83E\uDDF9",
            items = listOf(
                SnagItem(1, "Offsets observed on flooring"),
                SnagItem(2, "Hallowness"),
                SnagItem(3, "Wall tiles cutouts not finished properly"),
                SnagItem(4, "Wall tiles grouting pending"),
                SnagItem(5, "Chippoffs and gaps observed"),
                SnagItem(6, "Undulation observed"),
                SnagItem(7, "Shade variation"),
                SnagItem(8, "Finishing pending looking odd Shaft"),
                SnagItem(9, "Frame & Shutter polish"),
                SnagItem(10, "Scarthes on shutter"),
                SnagItem(11, "Damage on frame"),
                SnagItem(12, "Hinges not screwed properly")
            )
        ),
        SnagSection(
            id = "living-area",
            title = "Living Area",
            icon = "\uD83C\uDFE0",
            items = listOf(
                SnagItem(1, "Hallowness observed on floor tiles"),
                SnagItem(2, "Pooja walls is not in line"),
                SnagItem(3, "Tile damage"),
                SnagItem(4, "Wall edges finishing not done properly"),
                SnagItem(5, "Main door Frame damage"),
                SnagItem(6, "Frame gaps and polish not finished properly"),
                SnagItem(7, "Fastner capsmissing"),
                SnagItem(8, "Eye piece missing in shutter"),
                SnagItem(9, "Window Mesh Damaged"),
                SnagItem(10, "DB gaps observed"),
                SnagItem(11, "DB cover drawing misiing")
            )
        ),
        SnagSection(
            id = "balcony",
            title = "Balcony",
            icon = "\uD83C\uDF3F",
            items = listOf(
                SnagItem(1, "Skirting and flooring tiles gaps"),
                SnagItem(2, "Skirting gaps"),
                SnagItem(3, "Upstand slab Crack"),
                SnagItem(4, "Shade variation"),
                SnagItem(5, "Sealant not applied properly and wall edges not finished properly"),
                SnagItem(6, "Mesh damages"),
                SnagItem(7, "Scarthes On glass"),
                SnagItem(8, "Railing rubber damage"),
                SnagItem(9, "Stains on Railing"),
                SnagItem(10, "Damping shade Observed on corners of walls")
            )
        )
    )

    val DIMENSION_DATA = listOf(
        DimensionData("Bedroom 1", "10 ft \u00D7 10 ft", "10 ft \u00D7 10 ft", "ok", "Satisfactory"),
        DimensionData("Bedroom 1 Toilet", "4.6 ft \u00D7 8.3 ft", "4.3 ft \u00D7 8 ft", "warn", "Discrepancy \u2014 Smaller than brochure"),
        DimensionData("Master Bedroom", "10 ft \u00D7 12.6 ft", "10 ft \u00D7 12.5 ft", "ok", "Satisfactory"),
        DimensionData("Master Toilet", "8.3 ft \u00D7 4.6 ft", "8 ft \u00D7 4.4 ft", "warn", "Discrepancy \u2014 Smaller than brochure"),
        DimensionData("Bedroom 2", "11.6 ft \u00D7 10 ft", "11.5 ft \u00D7 10 ft", "ok", "Satisfactory"),
        DimensionData("Bedroom 2 Toilet", "6.9 ft \u00D7 5 ft", "6.6 ft \u00D7 4.7 ft", "warn", "Discrepancy \u2014 Smaller than brochure"),
        DimensionData("Home Office", "10.2 ft \u00D7 10 ft", "10 ft \u00D7 10 ft", "ok", "Satisfactory"),
        DimensionData("Kitchen", "7.9 ft \u00D7 12 ft", "7.7 ft \u00D7 11.10 ft", "ok", "Satisfactory"),
        DimensionData("Utility", "4 ft \u00D7 7.9 ft", "4 ft \u00D7 6.4 ft", "warn", "Discrepancy \u2014 Smaller than brochure"),
        DimensionData("Drawing", "14.3 ft \u00D7 13 ft", "14.2 ft \u00D7 13 ft", "ok", "Satisfactory"),
        DimensionData("Family", "10 ft \u00D7 30.1 ft", "10 ft \u00D7 27.8 ft", "ok", "Satisfactory"),
        DimensionData("Balcony", "10 ft \u00D7 6 ft", "10 ft \u00D7 6.4 ft", "ok", "Satisfactory")
    )

    val totalItems: Int get() = SNAG_DATA.sumOf { it.items.size }
}

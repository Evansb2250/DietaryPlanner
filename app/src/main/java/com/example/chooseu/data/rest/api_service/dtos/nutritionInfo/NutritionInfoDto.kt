data class NutritionInfo(
    val uri: String = "",
    val calories: Double = 0.0,
    val totalWeight: Double = 0.0,
    val dietLabels: List<String> = emptyList(),
    val healthLabels: List<String> = emptyList(),
    val cautions: List<String> = emptyList(),
    val totalNutrients: TotalNutrientsDTO = TotalNutrientsDTO(),
    val totalDaily: TotalDaily = TotalDaily(),
    val ingredients: List<IngredientDTO> = emptyList()
)

data class TotalNutrientsDTO(
    val ENERC_KCAL: NutrientDTO = NutrientDTO(),
    val FAT: NutrientDTO = NutrientDTO(),
    val FASAT: NutrientDTO = NutrientDTO(),
    val FAMS: NutrientDTO = NutrientDTO(),
    val FAPU: NutrientDTO = NutrientDTO(),
    val CHOCDF: NutrientDTO = NutrientDTO(),
    val CHOCDFnet: NutrientDTO = NutrientDTO(),
    val FIBTG: NutrientDTO = NutrientDTO(),
    val SUGAR: NutrientDTO = NutrientDTO(),
    val PROCNT: NutrientDTO = NutrientDTO(),
    val CHOLE: NutrientDTO = NutrientDTO(),
    val NA: NutrientDTO = NutrientDTO(),
    val CA: NutrientDTO = NutrientDTO(),
    val MG: NutrientDTO = NutrientDTO(),
    val K: NutrientDTO = NutrientDTO(),
    val FE: NutrientDTO = NutrientDTO(),
    val ZN: NutrientDTO = NutrientDTO(),
    val P: NutrientDTO = NutrientDTO(),
    val VITA_RAE: NutrientDTO = NutrientDTO(),
    val VITC: NutrientDTO = NutrientDTO(),
    val THIA: NutrientDTO = NutrientDTO(),
    val RIBF: NutrientDTO = NutrientDTO(),
    val NIA: NutrientDTO = NutrientDTO(),
    val VITB6A: NutrientDTO = NutrientDTO(),
    val FOLDFE: NutrientDTO = NutrientDTO(),
    val FOLFD: NutrientDTO = NutrientDTO(),
    val FOLAC: NutrientDTO = NutrientDTO(),
    val VITB12: NutrientDTO = NutrientDTO(),
    val VITD: NutrientDTO = NutrientDTO(),
    val TOCPHA: NutrientDTO = NutrientDTO(),
    val VITK1: NutrientDTO = NutrientDTO(),
    val WATER: NutrientDTO = NutrientDTO()
)

data class NutrientDTO(
    val label: String = "",
    val quantity: Double = 0.0,
    val unit: String = ""
)

data class TotalDaily(
    val ENERC_KCAL: DailyNutrientDTO = DailyNutrientDTO(),
    val FAT: DailyNutrientDTO = DailyNutrientDTO(),
    val FASAT: DailyNutrientDTO = DailyNutrientDTO(),
    val CHOCDF: DailyNutrientDTO = DailyNutrientDTO(),
    val FIBTG: DailyNutrientDTO = DailyNutrientDTO(),
    val PROCNT: DailyNutrientDTO = DailyNutrientDTO(),
    val CHOLE: DailyNutrientDTO = DailyNutrientDTO(),
    val NA: DailyNutrientDTO = DailyNutrientDTO(),
    val CA: DailyNutrientDTO = DailyNutrientDTO(),
    val MG: DailyNutrientDTO = DailyNutrientDTO(),
    val K: DailyNutrientDTO = DailyNutrientDTO(),
    val FE: DailyNutrientDTO = DailyNutrientDTO(),
    val ZN: DailyNutrientDTO = DailyNutrientDTO(),
    val P: DailyNutrientDTO = DailyNutrientDTO(),
    val VITA_RAE: DailyNutrientDTO = DailyNutrientDTO(),
    val VITC: DailyNutrientDTO = DailyNutrientDTO(),
    val THIA: DailyNutrientDTO = DailyNutrientDTO(),
    val RIBF: DailyNutrientDTO = DailyNutrientDTO(),
    val NIA: DailyNutrientDTO = DailyNutrientDTO(),
    val VITB6A: DailyNutrientDTO = DailyNutrientDTO(),
    val FOLDFE: DailyNutrientDTO = DailyNutrientDTO(),
    val VITB12: DailyNutrientDTO = DailyNutrientDTO(),
    val VITD: DailyNutrientDTO = DailyNutrientDTO(),
    val TOCPHA: DailyNutrientDTO = DailyNutrientDTO(),
    val VITK1: DailyNutrientDTO = DailyNutrientDTO()
)

data class DailyNutrientDTO(
    val label: String = "",
    val quantity: Double = 0.0,
    val unit: String = ""
)

data class IngredientDTO(
    val parsed: List<ParsedIngredientDTO> = emptyList()
)

data class ParsedIngredientDTO(
    val quantity: Int = 0,
    val measure: String = "",
    val food: String = "",
    val foodId: String = "",
    val weight: Double = 0.0,
    val retainedWeight: Double = 0.0,
    val servingsPerContainer: Double = 0.0,
    val measureURI: String = "",
    val status: String = ""
)
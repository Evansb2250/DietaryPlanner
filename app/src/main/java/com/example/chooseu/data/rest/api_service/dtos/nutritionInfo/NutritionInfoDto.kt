import com.example.chooseu.ui.screens.nutrition_screen.NutritionDetail
import com.example.chooseu.utils.NumberUtils
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.DecimalFormat

@Serializable
data class NutritionInfo(
    @SerialName("uri") val uri: String = "",
    @SerialName("calories") val calories: Double = 0.0,
    @SerialName("totalWeight") val totalWeight: Double = 0.0,
    @SerialName("dietLabels") val dietLabels: List<String> = emptyList(),
    @SerialName("healthLabels") val healthLabels: List<String> = emptyList(),
    @SerialName("cautions") val cautions: List<String> = emptyList(),
    @SerialName("totalNutrients") val totalNutrients: TotalNutrientsDTO = TotalNutrientsDTO(),
    @SerialName("totalDaily") val totalDaily: TotalDailyDTO = TotalDailyDTO(),
    @SerialName("ingredients") val ingredients: List<IngredientDTO> = emptyList()
)

fun TotalNutrientsDTO.toNutritionDetail(
    servingType: String,
    quantity: String = "1.0",
): NutritionDetail = NutritionDetail(
    servingType = servingType,
    quantifier = NumberUtils.updateStringToValidNumber(quantity),
    nutritionValues = listOf(
        this.enercKCAL.copy(
            label = "Calories",
        ),
        this.fasat.copy(
            label = "Saturated fat"
        ),
        this.fatrn.copy(
            label = "Trans fat"
        ),
        this.cholesterol.copy(
            label = "Cholestrol"
        ),
        this.sodium.copy(
            label = "Sodium"
        ),
        this.chocdfNet.copy(
            label = "Total carbs"
        ),
        this.fiber.copy(
            label = "Fiber",
        ),
        this.sugar.copy(
            label = "Sugar",
        ),
        this.addedSugar.copy(
            label = "Added Sugar",
        ),
        this.procnt.copy(
            label = "Protein",
        ),
        this.vitaminC.copy(
            label = "Vitamin C",
        ),
        this.iron.copy(
            label = "Iron"
        ),
        this.vitaminD.copy(
            label = "Vitamin D"
        ),
        this.potassium.copy(
            label = "Potassium"
        ),
        this.calcium.copy(
            label = "Calcium"
        )
    ),
)


@Serializable
data class TotalNutrientsDTO(
    @SerialName("SUGAR.added") val addedSugar: NutrientDTO = NutrientDTO(),
    @SerialName("SUGAR") val sugar: NutrientDTO = NutrientDTO(),
    @SerialName("ENERC_KCAL") val enercKCAL: NutrientDTO = NutrientDTO(),
    @SerialName("FAT") val fat: NutrientDTO = NutrientDTO(),
    @SerialName("FATRN") val fatrn: NutrientDTO = NutrientDTO(),
    @SerialName("FASAT") val fasat: NutrientDTO = NutrientDTO(),
    @SerialName("FAMS") val fams: NutrientDTO = NutrientDTO(),
    @SerialName("FAPU") val fapu: NutrientDTO = NutrientDTO(),
    @SerialName("CHOCDF") val chocdf: NutrientDTO = NutrientDTO(),
    @SerialName("FIBTG") val fiber: NutrientDTO = NutrientDTO(),
    @SerialName("CHOCDF.net") val chocdfNet: NutrientDTO = NutrientDTO(),
    @SerialName("PROCNT") val procnt: NutrientDTO = NutrientDTO(),
    @SerialName("CHOLE") val cholesterol: NutrientDTO = NutrientDTO(),
    @SerialName("NA") val sodium: NutrientDTO = NutrientDTO(),
    @SerialName("CA") val calcium: NutrientDTO = NutrientDTO(),
    @SerialName("MG") val magnesium: NutrientDTO = NutrientDTO(),
    @SerialName("K") val potassium: NutrientDTO = NutrientDTO(),
    @SerialName("FE") val iron: NutrientDTO = NutrientDTO(),
    @SerialName("ZN") val zinc: NutrientDTO = NutrientDTO(),
    @SerialName("P") val phosphorus: NutrientDTO = NutrientDTO(),
    @SerialName("VITA_RAE") val vitaminARAE: NutrientDTO = NutrientDTO(),
    @SerialName("VITC") val vitaminC: NutrientDTO = NutrientDTO(),
    @SerialName("THIA") val thiamin: NutrientDTO = NutrientDTO(),
    @SerialName("RIBF") val riboflavin: NutrientDTO = NutrientDTO(),
    @SerialName("NIA") val niacin: NutrientDTO = NutrientDTO(),
    @SerialName("VITB6A") val vitaminB6A: NutrientDTO = NutrientDTO(),
    @SerialName("FOLDFE") val folateDFE: NutrientDTO = NutrientDTO(),
    @SerialName("FOLFD") val folateFood: NutrientDTO = NutrientDTO(),
    @SerialName("FOLAC") val folicAcid: NutrientDTO = NutrientDTO(),
    @SerialName("VITB12") val vitaminB12: NutrientDTO = NutrientDTO(),
    @SerialName("VITD") val vitaminD: NutrientDTO = NutrientDTO(),
    @SerialName("TOCPHA") val vitaminE: NutrientDTO = NutrientDTO(),
    @SerialName("VITK1") val vitaminK1: NutrientDTO = NutrientDTO(),
    @SerialName("WATER") val water: NutrientDTO = NutrientDTO()
)

@Serializable
data class NutrientDTO(
    val label: String = "",
    val quantity: Double = 0.0,
    val unit: String? = null
)

@Serializable
data class TotalDailyDTO(
    @SerialName("ENERC_KCAL") val enercKCAL: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("FAT") val fat: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("FASAT") val fasat: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("CHOCDF") val chocdf: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("FIBTG") val fibtg: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("PROCNT") val procnt: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("CHOLE") val cholesterol: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("NA") val sodium: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("CA") val calcium: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("MG") val magnesium: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("K") val potassium: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("FE") val iron: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("ZN") val zinc: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("P") val phosphorus: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("VITA_RAE") val vitaminARAE: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("VITC") val vitaminC: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("THIA") val thiamin: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("RIBF") val riboflavin: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("NIA") val niacin: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("VITB6A") val vitaminB6A: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("FOLDFE") val folateDFE: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("VITB12") val vitaminB12: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("VITD") val vitaminD: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("TOCPHA") val vitaminE: DailyNutrientDTO = DailyNutrientDTO(),
    @SerialName("VITK1") val vitaminK1: DailyNutrientDTO = DailyNutrientDTO()
)

@Serializable
data class DailyNutrientDTO(
    val label: String = "",
    val quantity: Double = 0.0,
    val unit: String = ""
)

@Serializable
data class IngredientDTO(
    val parsed: List<ParsedIngredientDTO> = emptyList()
)

@Serializable
data class ParsedIngredientDTO(
    val quantity: Double = 0.0,
    val measure: String = "",
    val food: String = "",
    val foodId: String = "",
    val weight: Double = 0.0,
    val retainedWeight: Double = 0.0,
    val servingsPerContainer: Double = 0.0,
    val measureURI: String = "",
    val status: String = ""
)
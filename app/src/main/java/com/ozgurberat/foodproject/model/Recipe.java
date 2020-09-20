package com.ozgurberat.foodproject.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "recipe_table")
public class Recipe {

    @PrimaryKey
    @SerializedName("idMeal")
    @NonNull
    private String mealId;

    @SerializedName("strMeal")
    private String mealName;

    @SerializedName("strMealThumb")
    private String mealImage;

    @SerializedName("strYoutube")
    private String mealYoutubeLink;

    @SerializedName("strSource")
    private String mealSource;

    @SerializedName("strInstructions")
    private String mealInstructions;

    @SerializedName("strIngredient1")
    private String mealIngredient1;

    @SerializedName("strIngredient2")
    private String mealIngredient2;

    @SerializedName("strIngredient3")
    private String mealIngredient3;

    @SerializedName("strIngredient4")
    private String mealIngredient4;

    @SerializedName("strIngredient5")
    private String mealIngredient5;

    @SerializedName("strIngredient6")
    private String mealIngredient6;

    @SerializedName("strIngredient7")
    private String mealIngredient7;

    @SerializedName("strIngredient8")
    private String mealIngredient8;

    @SerializedName("strIngredient9")
    private String mealIngredient9;

    @SerializedName("strIngredient10")
    private String mealIngredient10;

    @SerializedName("strIngredient11")
    private String mealIngredient11;

    @SerializedName("strIngredient12")
    private String mealIngredient12;

    @SerializedName("strIngredient13")
    private String mealIngredient13;

    @SerializedName("strIngredient14")
    private String mealIngredient14;

    @SerializedName("strIngredient15")
    private String mealIngredient15;

    @SerializedName("strIngredient16")
    private String mealIngredient16;

    @SerializedName("strIngredient17")
    private String mealIngredient17;

    @SerializedName("strIngredient18")
    private String mealIngredient18;

    @SerializedName("strIngredient19")
    private String mealIngredient19;

    @SerializedName("strIngredient20")
    private String mealIngredient20;

    @SerializedName("strMeasure1")
    private String mealMeasure1;

    @SerializedName("strMeasure2")
    private String mealMeasure2;

    @SerializedName("strMeasure3")
    private String mealMeasure3;

    @SerializedName("strMeasure4")
    private String mealMeasure4;

    @SerializedName("strMeasure5")
    private String mealMeasure5;

    @SerializedName("strMeasure6")
    private String mealMeasure6;

    @SerializedName("strMeasure7")
    private String mealMeasure7;

    @SerializedName("strMeasure8")
    private String mealMeasure8;

    @SerializedName("strMeasure9")
    private String mealMeasure9;

    @SerializedName("strMeasure10")
    private String mealMeasure10;

    @SerializedName("strMeasure11")
    private String mealMeasure11;

    @SerializedName("strMeasure12")
    private String mealMeasure12;

    @SerializedName("strMeasure13")
    private String mealMeasure13;

    @SerializedName("strMeasure14")
    private String mealMeasure14;

    @SerializedName("strMeasure15")
    private String mealMeasure15;

    @SerializedName("strMeasure16")
    private String mealMeasure16;

    @SerializedName("strMeasure17")
    private String mealMeasure17;

    @SerializedName("strMeasure18")
    private String mealMeasure18;

    @SerializedName("strMeasure19")
    private String mealMeasure19;

    @SerializedName("strMeasure20")
    private String mealMeasure20;

    public Recipe(String mealId, String mealName, String mealImage, String mealYoutubeLink, String mealSource, String mealInstructions, String mealIngredient1, String mealIngredient2, String mealIngredient3, String mealIngredient4, String mealIngredient5, String mealIngredient6, String mealIngredient7, String mealIngredient8, String mealIngredient9, String mealIngredient10, String mealIngredient11, String mealIngredient12, String mealIngredient13, String mealIngredient14, String mealIngredient15, String mealIngredient16, String mealIngredient17, String mealIngredient18, String mealIngredient19, String mealIngredient20, String mealMeasure1, String mealMeasure2, String mealMeasure3, String mealMeasure4, String mealMeasure5, String mealMeasure6, String mealMeasure7, String mealMeasure8, String mealMeasure9, String mealMeasure10, String mealMeasure11, String mealMeasure12, String mealMeasure13, String mealMeasure14, String mealMeasure15, String mealMeasure16, String mealMeasure17, String mealMeasure18, String mealMeasure19, String mealMeasure20) {
        this.mealId = mealId;
        this.mealName = mealName;
        this.mealImage = mealImage;
        this.mealYoutubeLink = mealYoutubeLink;
        this.mealSource = mealSource;
        this.mealInstructions = mealInstructions;
        this.mealIngredient1 = mealIngredient1;
        this.mealIngredient2 = mealIngredient2;
        this.mealIngredient3 = mealIngredient3;
        this.mealIngredient4 = mealIngredient4;
        this.mealIngredient5 = mealIngredient5;
        this.mealIngredient6 = mealIngredient6;
        this.mealIngredient7 = mealIngredient7;
        this.mealIngredient8 = mealIngredient8;
        this.mealIngredient9 = mealIngredient9;
        this.mealIngredient10 = mealIngredient10;
        this.mealIngredient11 = mealIngredient11;
        this.mealIngredient12 = mealIngredient12;
        this.mealIngredient13 = mealIngredient13;
        this.mealIngredient14 = mealIngredient14;
        this.mealIngredient15 = mealIngredient15;
        this.mealIngredient16 = mealIngredient16;
        this.mealIngredient17 = mealIngredient17;
        this.mealIngredient18 = mealIngredient18;
        this.mealIngredient19 = mealIngredient19;
        this.mealIngredient20 = mealIngredient20;
        this.mealMeasure1 = mealMeasure1;
        this.mealMeasure2 = mealMeasure2;
        this.mealMeasure3 = mealMeasure3;
        this.mealMeasure4 = mealMeasure4;
        this.mealMeasure5 = mealMeasure5;
        this.mealMeasure6 = mealMeasure6;
        this.mealMeasure7 = mealMeasure7;
        this.mealMeasure8 = mealMeasure8;
        this.mealMeasure9 = mealMeasure9;
        this.mealMeasure10 = mealMeasure10;
        this.mealMeasure11 = mealMeasure11;
        this.mealMeasure12 = mealMeasure12;
        this.mealMeasure13 = mealMeasure13;
        this.mealMeasure14 = mealMeasure14;
        this.mealMeasure15 = mealMeasure15;
        this.mealMeasure16 = mealMeasure16;
        this.mealMeasure17 = mealMeasure17;
        this.mealMeasure18 = mealMeasure18;
        this.mealMeasure19 = mealMeasure19;
        this.mealMeasure20 = mealMeasure20;
    }

    public String getMealId() {
        return mealId;
    }

    public String getMealName() {
        return mealName;
    }

    public String getMealImage() {
        return mealImage;
    }

    public String getMealYoutubeLink() {
        return mealYoutubeLink;
    }

    public String getMealSource() {
        return mealSource;
    }

    public String getMealInstructions() {
        return mealInstructions;
    }

    public String getMealIngredient1() {
        return mealIngredient1;
    }

    public String getMealIngredient2() {
        return mealIngredient2;
    }

    public String getMealIngredient3() {
        return mealIngredient3;
    }

    public String getMealIngredient4() {
        return mealIngredient4;
    }

    public String getMealIngredient5() {
        return mealIngredient5;
    }

    public String getMealIngredient6() {
        return mealIngredient6;
    }

    public String getMealIngredient7() {
        return mealIngredient7;
    }

    public String getMealIngredient8() {
        return mealIngredient8;
    }

    public String getMealIngredient9() {
        return mealIngredient9;
    }

    public String getMealIngredient10() {
        return mealIngredient10;
    }

    public String getMealIngredient11() {
        return mealIngredient11;
    }

    public String getMealIngredient12() {
        return mealIngredient12;
    }

    public String getMealIngredient13() {
        return mealIngredient13;
    }

    public String getMealIngredient14() {
        return mealIngredient14;
    }

    public String getMealIngredient15() {
        return mealIngredient15;
    }

    public String getMealIngredient16() {
        return mealIngredient16;
    }

    public String getMealIngredient17() {
        return mealIngredient17;
    }

    public String getMealIngredient18() {
        return mealIngredient18;
    }

    public String getMealIngredient19() {
        return mealIngredient19;
    }

    public String getMealIngredient20() {
        return mealIngredient20;
    }

    public String getMealMeasure1() {
        return mealMeasure1;
    }

    public String getMealMeasure2() {
        return mealMeasure2;
    }

    public String getMealMeasure3() {
        return mealMeasure3;
    }

    public String getMealMeasure4() {
        return mealMeasure4;
    }

    public String getMealMeasure5() {
        return mealMeasure5;
    }

    public String getMealMeasure6() {
        return mealMeasure6;
    }

    public String getMealMeasure7() {
        return mealMeasure7;
    }

    public String getMealMeasure8() {
        return mealMeasure8;
    }

    public String getMealMeasure9() {
        return mealMeasure9;
    }

    public String getMealMeasure10() {
        return mealMeasure10;
    }

    public String getMealMeasure11() {
        return mealMeasure11;
    }

    public String getMealMeasure12() {
        return mealMeasure12;
    }

    public String getMealMeasure13() {
        return mealMeasure13;
    }

    public String getMealMeasure14() {
        return mealMeasure14;
    }

    public String getMealMeasure15() {
        return mealMeasure15;
    }

    public String getMealMeasure16() {
        return mealMeasure16;
    }

    public String getMealMeasure17() {
        return mealMeasure17;
    }

    public String getMealMeasure18() {
        return mealMeasure18;
    }

    public String getMealMeasure19() {
        return mealMeasure19;
    }

    public String getMealMeasure20() {
        return mealMeasure20;
    }

    @Override
    public String toString() {
        return "Recipe{" +
                "mealId='" + mealId + '\'' +
                ", mealName='" + mealName + '\'' +
                ", mealImage='" + mealImage + '\'' +
                ", mealYoutubeLink='" + mealYoutubeLink + '\'' +
                ", mealSource='" + mealSource + '\'' +
                ", mealInstructions='" + mealInstructions + '\'' +
                ", mealIngredient1='" + mealIngredient1 + '\'' +
                ", mealIngredient2='" + mealIngredient2 + '\'' +
                ", mealIngredient3='" + mealIngredient3 + '\'' +
                ", mealIngredient4='" + mealIngredient4 + '\'' +
                ", mealIngredient5='" + mealIngredient5 + '\'' +
                ", mealIngredient6='" + mealIngredient6 + '\'' +
                ", mealIngredient7='" + mealIngredient7 + '\'' +
                ", mealIngredient8='" + mealIngredient8 + '\'' +
                ", mealIngredient9='" + mealIngredient9 + '\'' +
                ", mealIngredient10='" + mealIngredient10 + '\'' +
                ", mealIngredient11='" + mealIngredient11 + '\'' +
                ", mealIngredient12='" + mealIngredient12 + '\'' +
                ", mealIngredient13='" + mealIngredient13 + '\'' +
                ", mealIngredient14='" + mealIngredient14 + '\'' +
                ", mealIngredient15='" + mealIngredient15 + '\'' +
                ", mealIngredient16='" + mealIngredient16 + '\'' +
                ", mealIngredient17='" + mealIngredient17 + '\'' +
                ", mealIngredient18='" + mealIngredient18 + '\'' +
                ", mealIngredient19='" + mealIngredient19 + '\'' +
                ", mealIngredient20='" + mealIngredient20 + '\'' +
                ", mealMeasure1='" + mealMeasure1 + '\'' +
                ", mealMeasure2='" + mealMeasure2 + '\'' +
                ", mealMeasure3='" + mealMeasure3 + '\'' +
                ", mealMeasure4='" + mealMeasure4 + '\'' +
                ", mealMeasure5='" + mealMeasure5 + '\'' +
                ", mealMeasure6='" + mealMeasure6 + '\'' +
                ", mealMeasure7='" + mealMeasure7 + '\'' +
                ", mealMeasure8='" + mealMeasure8 + '\'' +
                ", mealMeasure9='" + mealMeasure9 + '\'' +
                ", mealMeasure10='" + mealMeasure10 + '\'' +
                ", mealMeasure11='" + mealMeasure11 + '\'' +
                ", mealMeasure12='" + mealMeasure12 + '\'' +
                ", mealMeasure13='" + mealMeasure13 + '\'' +
                ", mealMeasure14='" + mealMeasure14 + '\'' +
                ", mealMeasure15='" + mealMeasure15 + '\'' +
                ", mealMeasure16='" + mealMeasure16 + '\'' +
                ", mealMeasure17='" + mealMeasure17 + '\'' +
                ", mealMeasure18='" + mealMeasure18 + '\'' +
                ", mealMeasure19='" + mealMeasure19 + '\'' +
                ", mealMeasure20='" + mealMeasure20 + '\'' +
                '}';
    }
}

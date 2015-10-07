package richt111.nutritioninfo.models;


public class NutritionResultModel {

    public NutritionHitsModel[] hits;

    public class NutritionHitsModel {

        public NutritionFieldsModel fields;

        public class NutritionFieldsModel {

            public String item_name;
            public float nf_calories;
            public float nf_total_fat;
            public float nf_total_carbohydrate;
            public float nf_protein;
            public float nf_sugars;
            public float nf_dietary_fiber;
            public float nf_sodium;
            public float nf_cholesterol;
        }

    }

}

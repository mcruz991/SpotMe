package com.example.SpotMe;

    public class ItemModel {
        private  int image;
        private  String name; //age, location;
        private String userId;

        ItemModel(){

        }

        public ItemModel(String userId, int image, String name) {
            this.userId = userId;
            this.image = image;
            this.name = name;


        }


        public String getUserId(){
            return this.userId = userId; }

        public void setUserId(String userId){
            this.userId = userId;
        }

        public int getImage() {
            return image;
        }

        public String getName() {
            return name;
        }



        /*public String getUsia() {
            return age;
        }

        public String getKota() {
            return location;
        } */
    }


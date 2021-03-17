package com.example.SpotMe;

    public class ItemModel {
        private int image;
        private String name, age, location;
        private String userId;


        public ItemModel() {
        }

        public ItemModel(int image, String name, String userId/*String age, String */) {
            this.userId = userId;
            this.image = image;
            this.name = name;
          //  this.age = age;
          //  this.location = location;
        }




        public String getUserId(){ return userId;}

        public void setUserId(String userId){this.userId = userId;}

        public int getImage() {
            return image;
        }

        public String getNama() {
            return name;
        }

        public String getUsia() {
            return age;
        }

        public String getKota() {
            return location;
        }
    }


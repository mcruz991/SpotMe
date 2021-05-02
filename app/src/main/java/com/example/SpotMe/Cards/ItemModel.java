package com.example.SpotMe.Cards;

    public class ItemModel {
       // private  int image;
        private  String name; //age, location;
        private String userId;
        private String profileImgUrl;
        private String age;
        public ItemModel(){

        }

        public ItemModel(String userId,  String name, String age, String profileImgUrl) {
            this.userId = userId;

            this.age = age;
            this.name = name;
            this.profileImgUrl = profileImgUrl;


        }


        public String getUserId(){
            return this.userId = userId; }

        public void setUserId(String userId){
            this.userId = userId;
        }



        public String getAge() {
            return age;
        }

        public String getName() {
            return name;
        }

        public String getProfileImgUrl() {
            return profileImgUrl;
        }


        /*public String getUsia() {
            return age;
        }

        public String getKota() {
            return location;
        } */
    }


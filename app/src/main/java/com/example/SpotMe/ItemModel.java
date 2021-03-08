package com.example.SpotMe;

    public class ItemModel {
        private int image;
        private String name, age, location;

        public ItemModel() {
        }

        public ItemModel(int image, String name/*String age, String */) {
            this.image = image;
            this.name = name;
          //  this.age = age;
          //  this.location = location;
        }



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


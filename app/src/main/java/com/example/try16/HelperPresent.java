package com.example.try16;

public class HelperPresent {



        private String rollNo;
        private String presentCount;


        public HelperPresent() {

            this.presentCount= String.valueOf(0);
        }

        public HelperPresent(String rollNo,String PresentCountCount) {
            this.rollNo = rollNo;
            this.presentCount=presentCount;
        }

        // Getters and setters
        public String getRollNo() {
            return rollNo;
        }

        public void setRollNo(String rollNo) {
            this.rollNo = rollNo;
        }


        public String getPresentCount() {
            return presentCount;
        }

        public void setPresentCount(String PresentCount) {
            this.presentCount = presentCount;
        }
    }


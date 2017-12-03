package com.pace.edustream;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pace on 4/10/17.
 */

public class ModelData implements Serializable {


    private String pass_key;
    private String encrypt_code;
    private String folder_path;
    private ArrayList<SubjectBean> subject;


    public String getPass_key() {
        return pass_key;
    }

    public void setPass_key(String pass_key) {
        this.pass_key = pass_key;
    }

    public String getEncrypt_code() {
        return encrypt_code;
    }

    public void setEncrypt_code(String encrypt_code) {
        this.encrypt_code = encrypt_code;
    }

    public String getFolder_path() {
        return folder_path;
    }

    public void setFolder_path(String folder_path) {
        this.folder_path = folder_path;
    }

    public ArrayList<SubjectBean> getSubject() {
        return subject;
    }

    public void setSubject(ArrayList<SubjectBean> subject) {
        this.subject = subject;
    }

    public static class SubjectBean implements Serializable {
        /**
         * name : Maths
         * units : [{"unit_id":"1","unit_name":"RELATIONS AND FUNCTIONS","chapter":[{"chapter_id":"1","chapter_name":"Relations and Functions","chapter_hours":"3hours"},{"chapter_id":"2","chapter_name":"Inverse Trigonometric Functions","chapter_hours":"3hours"}]},{"unit_id":"2","unit_name":"ALGEBRA","chapter":[{"chapter_id":"1","chapter_name":"Matrices","chapter_hours":"3hours"},{"chapter_id":"2","chapter_name":"Determinants","chapter_hours":"3hours"}]},{"unit_id":"3","unit_name":"CALCULUS","chapter":[{"chapter_id":"1","chapter_name":"Continuity and Differentiability","chapter_hours":"3hours"},{"chapter_id":"2","chapter_name":"Applications of Derivatives","chapter_hours":"3hours"},{"chapter_id":"3","chapter_name":"Integrals","chapter_hours":"3hours"},{"chapter_id":"4","chapter_name":"Applications of the Integrals","chapter_hours":"3hours"},{"chapter_id":"5","chapter_name":"Differential Equations","chapter_hours":"3hours"}]},{"unit_id":"4","unit_name":"VECTORS AND THREE-DIMENSIONAL GEOMETRY","chapter":[{"chapter_id":"1","chapter_name":"Vectors","chapter_hours":"3hours"},{"chapter_id":"2","chapter_name":"Three-dimensional Geometry","chapter_hours":"3hours"}]},{"unit_id":"5","unit_name":"LINEAR PROGRAMMING","chapter":[{"chapter_id":"1","chapter_name":"Introduction of L.P.P.","chapter_hours":"3hours"}]},{"unit_id":"6","unit_name":"PROBABILITY","chapter":[{"chapter_id":"1","chapter_name":"Conditional probability","chapter_hours":"3hours"}]}]
         */

        private String name;
        private ArrayList<UnitsBean> units;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ArrayList<UnitsBean> getUnits() {
            return units;
        }

        public void setUnits(ArrayList<UnitsBean> units) {
            this.units = units;
        }

        public static class UnitsBean {
            /**
             * unit_id : 1
             * unit_name : RELATIONS AND FUNCTIONS
             * chapter : [{"chapter_id":"1","chapter_name":"Relations and Functions","chapter_hours":"3hours"},{"chapter_id":"2","chapter_name":"Inverse Trigonometric Functions","chapter_hours":"3hours"}]
             */

            private String unit_id;
            private String unit_name;
            private ArrayList<ChapterBean> chapter;

            public String getUnit_id() {
                return unit_id;
            }

            public void setUnit_id(String unit_id) {
                this.unit_id = unit_id;
            }

            public String getUnit_name() {
                return unit_name;
            }

            public void setUnit_name(String unit_name) {
                this.unit_name = unit_name;
            }

            public ArrayList<ChapterBean> getChapter() {
                return chapter;
            }

            public void setChapter(ArrayList<ChapterBean> chapter) {
                this.chapter = chapter;
            }

            public static class ChapterBean implements Serializable{
                /**
                 * chapter_id : 1
                 * chapter_name : Relations and Functions
                 * chapter_hours : 3hours
                 */

                private String chapter_id;
                private String chapter_name;
                private String chapter_hours;
                private String chapter_path;
                private String chapter_key_name;


                public String getChapter_key_name() {
                    return chapter_key_name;
                }

                public void setChapter_key_name(String chapter_key_name) {
                    this.chapter_key_name = chapter_key_name;
                }

                public String getChapter_path() {
                    return chapter_path;
                }

                public void setChapter_path(String chapter_path) {
                    this.chapter_path = chapter_path;
                }

                public String getChapter_id() {
                    return chapter_id;
                }

                public void setChapter_id(String chapter_id) {
                    this.chapter_id = chapter_id;
                }

                public String getChapter_name() {
                    return chapter_name;
                }

                public void setChapter_name(String chapter_name) {
                    this.chapter_name = chapter_name;
                }

                public String getChapter_hours() {
                    return chapter_hours;
                }

                public void setChapter_hours(String chapter_hours) {
                    this.chapter_hours = chapter_hours;
                }
            }
        }
    }
}

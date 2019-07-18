package com.app.whoot.bean;

import java.util.List;

/**
 * Created by Sunrise on 3/21/2019.
 */

public class GoogleMapBean {


    /**
     * formatted_address : Futian CBD, Futian, Shenzhen, China, 518048
     * geometry : {"location":{"lat":22.534637,"lng":114.053196},"viewport":{"northeast":{"lat":22.53600272989272,"lng":114.0545452798927},"southwest":{"lat":22.53330307010728,"lng":114.0518456201073}}}
     * icon : https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png
     * id : b8f360a4609015d664f295350374495b9a02656d
     * name : 购物公园
     * photos : [{"height":2976,"html_attributions":["<a href=\"https://maps.google.com/maps/contrib/115715886528624786145/photos\">skychiu813<\/a>"],"photo_reference":"CmRaAAAAuLEglQS0hP8PZjlBjfLPeNPoy49AQSGsOJQuMrGiiUp4Wa-a704NyxkrlQWgmLBUSGOVhJAb7eyhsjAlRlj5_XdBTBB_yHDlu21WIENLP098hQi_lSuq9mc0uIKeWMH9EhC3iZEez_gvkP__F_uYn62nGhQsbYIkiiFPlmED0LJVry9ppVRkjA","width":3968}]
     * place_id : ChIJp-vlxwb0AzQRW8lHqb99VZs
     * plus_code : {"compound_code":"G3M3+V7 Futian, Shenzhen, Guangdong, China","global_code":"7PJPG3M3+V7"}
     * rating : 3
     * reference : ChIJp-vlxwb0AzQRW8lHqb99VZs
     * types : ["transit_station","point_of_interest","establishment"]
     * user_ratings_total : 1
     */

    private String formatted_address;
    private GeometryBean geometry;
    private String icon;
    private String id;
    private String name;
    private String place_id;
    private PlusCodeBean plus_code;
    private int rating;
    private String reference;
    private int user_ratings_total;
    private List<PhotosBean> photos;
    private List<String> types;

    public String getFormatted_address() {
        return formatted_address;
    }

    public void setFormatted_address(String formatted_address) {
        this.formatted_address = formatted_address;
    }

    public GeometryBean getGeometry() {
        return geometry;
    }

    public void setGeometry(GeometryBean geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public PlusCodeBean getPlus_code() {
        return plus_code;
    }

    public void setPlus_code(PlusCodeBean plus_code) {
        this.plus_code = plus_code;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getUser_ratings_total() {
        return user_ratings_total;
    }

    public void setUser_ratings_total(int user_ratings_total) {
        this.user_ratings_total = user_ratings_total;
    }

    public List<PhotosBean> getPhotos() {
        return photos;
    }

    public void setPhotos(List<PhotosBean> photos) {
        this.photos = photos;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public static class GeometryBean {
        /**
         * location : {"lat":22.534637,"lng":114.053196}
         * viewport : {"northeast":{"lat":22.53600272989272,"lng":114.0545452798927},"southwest":{"lat":22.53330307010728,"lng":114.0518456201073}}
         */

        private LocationBean location;
        private ViewportBean viewport;

        public LocationBean getLocation() {
            return location;
        }

        public void setLocation(LocationBean location) {
            this.location = location;
        }

        public ViewportBean getViewport() {
            return viewport;
        }

        public void setViewport(ViewportBean viewport) {
            this.viewport = viewport;
        }

        public static class LocationBean {
            /**
             * lat : 22.534637
             * lng : 114.053196
             */

            private double lat;
            private double lng;

            public double getLat() {
                return lat;
            }

            public void setLat(double lat) {
                this.lat = lat;
            }

            public double getLng() {
                return lng;
            }

            public void setLng(double lng) {
                this.lng = lng;
            }
        }

        public static class ViewportBean {
            /**
             * northeast : {"lat":22.53600272989272,"lng":114.0545452798927}
             * southwest : {"lat":22.53330307010728,"lng":114.0518456201073}
             */

            private NortheastBean northeast;
            private SouthwestBean southwest;

            public NortheastBean getNortheast() {
                return northeast;
            }

            public void setNortheast(NortheastBean northeast) {
                this.northeast = northeast;
            }

            public SouthwestBean getSouthwest() {
                return southwest;
            }

            public void setSouthwest(SouthwestBean southwest) {
                this.southwest = southwest;
            }

            public static class NortheastBean {
                /**
                 * lat : 22.53600272989272
                 * lng : 114.0545452798927
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class SouthwestBean {
                /**
                 * lat : 22.53330307010728
                 * lng : 114.0518456201073
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }
        }
    }

    public static class PlusCodeBean {
        /**
         * compound_code : G3M3+V7 Futian, Shenzhen, Guangdong, China
         * global_code : 7PJPG3M3+V7
         */

        private String compound_code;
        private String global_code;

        public String getCompound_code() {
            return compound_code;
        }

        public void setCompound_code(String compound_code) {
            this.compound_code = compound_code;
        }

        public String getGlobal_code() {
            return global_code;
        }

        public void setGlobal_code(String global_code) {
            this.global_code = global_code;
        }
    }

    public static class PhotosBean {
        /**
         * height : 2976
         * html_attributions : ["<a href=\"https://maps.google.com/maps/contrib/115715886528624786145/photos\">skychiu813<\/a>"]
         * photo_reference : CmRaAAAAuLEglQS0hP8PZjlBjfLPeNPoy49AQSGsOJQuMrGiiUp4Wa-a704NyxkrlQWgmLBUSGOVhJAb7eyhsjAlRlj5_XdBTBB_yHDlu21WIENLP098hQi_lSuq9mc0uIKeWMH9EhC3iZEez_gvkP__F_uYn62nGhQsbYIkiiFPlmED0LJVry9ppVRkjA
         * width : 3968
         */

        private int height;
        private String photo_reference;
        private int width;
        private List<String> html_attributions;

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getPhoto_reference() {
            return photo_reference;
        }

        public void setPhoto_reference(String photo_reference) {
            this.photo_reference = photo_reference;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public List<String> getHtml_attributions() {
            return html_attributions;
        }

        public void setHtml_attributions(List<String> html_attributions) {
            this.html_attributions = html_attributions;
        }
    }
}

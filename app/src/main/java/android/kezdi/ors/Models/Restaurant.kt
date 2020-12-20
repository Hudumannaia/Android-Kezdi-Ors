package android.kezdi.ors.Models

data class Restaurant(  var id: Int = 0,
                        var name: String = "",
                        var address: String = "",
                        var city: String = "",
                        var state: String = "",
                        var area: String = "",
                        var postal_code: String = "",
                        var country: String = "",
                        var phone: String = "",
                        var lat: Float = 0.0f,
                        var lng: Float = 0.0f,
                        var price: Int = 0,
                        var reserve_url: String = "",
                        var mobile_reserve_url: String = "",
                        var image_url: String = "")
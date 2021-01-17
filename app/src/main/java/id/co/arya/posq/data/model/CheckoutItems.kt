package id.co.arya.posq.data.model

data class CheckoutItems(
    val total: String,
    val costumername: String,
    val items: ArrayList<Cart>
)
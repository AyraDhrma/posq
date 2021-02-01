package id.co.arya.posq.data.model

data class CheckoutItems(
    val total: String,
    val userid: String,
    val costumername: String,
    val paymentmethod: String,
    val paymentrp: String,
    val items: ArrayList<Cart>
)
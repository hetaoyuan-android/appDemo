package com.example.test


fun main(){
    sayHello(name = "王子猪")
}
fun sayHello(prefix: String = "Mr.", name: String) {
    println("Hello, $prefix $name")
}
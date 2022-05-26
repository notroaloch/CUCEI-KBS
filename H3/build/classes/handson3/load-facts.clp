;;
;; HandsOn 3 - Comunicación Directa entre Agentes
;; Sistemas Basados en Conocimiento - CUCEI 2022A
;; Ochoa Herrera Rodrigo Alejandro
;;


(deffacts products
    (product (name IPHONE13) (category CELULAR))
    (product (name NOTE12) (category CELULAR))
    (product (name MACBOOK) (category COMPUTADORA))
)

(deffacts payments
    (payment (name BANAMEX) (offer "10% DE BONIFICACIÓN"))
    (payment (name LIVERPOOL) (offer "6 MESES SIN INTERESES"))
    (payment (name EFECTIVO) (offer "100 PESOS EN VALES POR CADA 1000 PESOS DE COMPRA"))
)

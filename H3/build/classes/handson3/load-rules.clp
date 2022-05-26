;;
;; HandsOn 3 - Comunicación Directa entre Agentes
;; Sistemas Basados en Conocimiento - CUCEI 2022A
;; Ochoa Herrera Rodrigo Alejandro
;;

;; iPhone 13 con BANAMEX
(defrule iphone13-banamex
    (order (order-id ?orderID) (product IPHONE13) (payment-method BANAMEX))
    =>
    (assert (offer (order-id ?orderID) (product IPHONE13) (message "24 MESES SIN INTERESES")))
)

;; Samsung Note 12 con LIVERPOOL
(defrule note12-liverpool
    (order (order-id ?orderID) (product NOTE12) (payment-method LIVERPOOL))
    =>
    (assert (offer (order-id ?orderID) (product NOTE12) (message "12 MESES SIN INTERESES")))
)

;; Compra Celular con EFECTIVO
(defrule celular-contado
    (order (order-id ?orderID) (product ?productName) (payment-method EFECTIVO))
    (product (name ?productName) (category CELULAR))
    =>
    (assert (offer (order-id ?orderID) (product ?productName) (message "15% DE DESCUENTO EN FUNDAS Y MICAS")))
)



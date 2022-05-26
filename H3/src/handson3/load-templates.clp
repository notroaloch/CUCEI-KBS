;;
;; HandsOn 3 - Comunicación Directa entre Agentes
;; Sistemas Basados en Conocimiento - CUCEI 2022A
;; Ochoa Herrera Rodrigo Alejandro
;;

(deftemplate product
    (slot name)
    (slot category)
)

(deftemplate payment
    (slot name)
    (slot offer)
)

(deftemplate order
    (slot order-id)
    (slot product)
    (slot payment-method)
)

(deftemplate offer
    (slot order-id)
    (slot product)
    (slot message)
)

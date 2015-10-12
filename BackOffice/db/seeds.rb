# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)
rfids = Rfid.create([
    {
        number: 24424,
        sia: 1,
        rua: "Rua Dr. Roberto Frias",
        passadeira: "",
        local: "",
        href: "https://www.google.pt/maps/place/R.+Dr.+Roberto+Frias,+4200+Porto/@41.1789015,-8.600912,17z/data=!3m1!4b1!4m2!3m1!1s0xd246440a21545dd:0x7734f3a151277396"
    },
    {
        number: 24425,
        sia: 2,
        rua: "Marquês do Pombal",
        passadeira: "",
        local: "",
        href: "https://www.google.pt/maps?q=38.72985000,-9.15136700"
    }
])

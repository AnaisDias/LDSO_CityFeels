class SearchController < ApplicationController
    layout false

    def index
        @rfids = Rfid.where(number: 24424)
    end

end

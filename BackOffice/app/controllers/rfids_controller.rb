class RfidsController < ApplicationController
    def index
        @rfids = Rfid.all
    end
end

class RfidsController < ApplicationController
    def index
        @rfids = Rfid.all
    end

    def update

    end

    def show
        @rfid = Rfid.find_by number: params[:id]
    end
end

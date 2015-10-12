class CreateRfids < ActiveRecord::Migration
  def change
    create_table :rfids do |t|
      t.integer :number
      t.integer :sia
      t.string :rua
      t.string :passadeira
      t.string :local
      t.string :href
      t.timestamps null: false
    end
  end
end

package com.example.hethonglora

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.hethonglora.databinding.ActivityGiaodienchinhBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class giaodienchinh : AppCompatActivity() {

    private lateinit var binding: ActivityGiaodienchinhBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var builder: AlertDialog.Builder
    private var mediaPlayer: MediaPlayer? = null

    var Temp1 = 40
    var Temp2 = 40
    var Hum1 = 70
    var Hum2 = 70
    var CO1 = 50
    var CO2 = 50
    var Bui1 = 30
    var Bui2 = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityGiaodienchinhBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        databaseReference = FirebaseDatabase.getInstance().reference
        builder = AlertDialog.Builder(this) //hiển thị hộp thoại tb

        binding.btnGG.setOnClickListener {
            val intent = Intent(this, GGsheet::class.java)
            startActivity(intent)
        }
        binding.btnthoat.setOnClickListener {
            finish() //thoát giao diện
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val Tem1Data = snapshot.child("Ngưỡng 1/Nhiệt độ").getValue(Double::class.java)
                val Hum1Data = snapshot.child("Ngưỡng 1/Độ ẩm không khí").getValue(Int::class.java)
                val CO1Data  = snapshot.child("Ngưỡng 1/Nồng độ CO").getValue(Double::class.java)
                val Bui1Data = snapshot.child("Ngưỡng 1/Bụi").getValue(Double::class.java)
                val Tem2Data = snapshot.child("Ngưỡng 2/Nhiệt độ").getValue(Double::class.java)
                val Hum2Data = snapshot.child("Ngưỡng 2/Độ ẩm không khí").getValue(Int::class.java)
                val CO2Data  = snapshot.child("Ngưỡng 2/Nồng độ CO").getValue(Double::class.java)
                val Bui2Data = snapshot.child("Ngưỡng 2/Bụi").getValue(Double::class.java)

                Temp1 = Tem1Data?.toInt() ?: Temp1 //gán lại cho các biến giới hạn
                Temp2 = Tem2Data?.toInt() ?: Temp2
                Hum1 = Hum1Data ?: Hum1
                Hum2 = Hum2Data ?: Hum2
                CO1 = CO1Data?.toInt() ?: CO1
                CO2 = CO2Data?.toInt() ?: CO2
                Bui1 = Bui1Data?.toInt() ?: Bui1
                Bui2 = Bui2Data?.toInt() ?: Bui2

                binding.tvTemp1.text = "$Temp1°C" //hiển thị lên textview
                binding.tvHum1.text = "$Hum1%"
                binding.tvCO1.text = CO1.toString()
                binding.tvBui1.text = Bui1.toString()
                binding.tvTemp2.text = "$Temp2°C"
                binding.tvHum2.text = "$Hum2%"
                binding.tvCO2.text = CO2.toString()
                binding.tvBui2.text = Bui2.toString()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
        //Xử lý sự kiện tvTemp1 khi ta nhấn vào để cài gh
        binding.tvTemp1.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cài giới hạn")
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.nhietdo_t1, null)
            val txtT1 = dialogView.findViewById<TextView>(R.id.txtT1)  //Cập nhật giá trị cài lên textview
            val sbNhietdo1 = dialogView.findViewById<SeekBar>(R.id.sbNhietdo1) //cập nhật giá trị trên thanh kéo seakbar
            txtT1.text=Temp1.toString()
            sbNhietdo1.progress=Temp1
            sbNhietdo1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    Temp1 = progress
                    txtT1.text = progress.toString()
                    databaseReference.child("Ngưỡng 1/Nhiệt độ").setValue(progress.toDouble())
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.drawable.bogochopthoai)
        }


        binding.tvTemp2.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cài giới hạn")
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.nhietdo_t2, null)
            val txtT2 = dialogView.findViewById<TextView>(R.id.txtT2)
            val sbNhietdo2 = dialogView.findViewById<SeekBar>(R.id.sbNhietdo2)
            txtT2.text=Temp2.toString()
            sbNhietdo2.progress=Temp2
            sbNhietdo2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    Temp2 = progress
                    txtT2.text = progress.toString()
                    databaseReference.child("Ngưỡng 2/Nhiệt độ").setValue(progress.toDouble())
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.drawable.bogochopthoai)
        }

        binding.tvHum1.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cài giới hạn")
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.doamkk_t1, null)
            val txtKK1 = dialogView.findViewById<TextView>(R.id.txtKK1)
            val sbDoamkk1 = dialogView.findViewById<SeekBar>(R.id.sbDoamkk1)
            txtKK1.text=Hum1.toString()
            sbDoamkk1.progress=Hum1
            sbDoamkk1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    Hum1 = progress
                    txtKK1.text = progress.toString()
                    databaseReference.child("Ngưỡng 1/Độ ẩm không khí").setValue(progress.toDouble())
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.drawable.bogochopthoai)
        }

        binding.tvHum2.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cài giới hạn")
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.doamkk_t2, null)
            val txtKK2 = dialogView.findViewById<TextView>(R.id.txtKK2)
            val sbDoamkk2 = dialogView.findViewById<SeekBar>(R.id.sbDoamkk2)
            txtKK2.text=Hum2.toString()
            sbDoamkk2.progress=Hum2
            sbDoamkk2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    Hum2 = progress
                    txtKK2.text = progress.toString()
                    databaseReference.child("Ngưỡng 2/Độ ẩm không khí").setValue(progress.toDouble())
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.drawable.bogochopthoai)
        }

        binding.tvCO1.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cài giới hạn")
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.co_t1, null)
            val txtCO1 = dialogView.findViewById<TextView>(R.id.txtCO1)
            val sbCO1 = dialogView.findViewById<SeekBar>(R.id.sbCO1)
            txtCO1.text=CO1.toString()
            sbCO1.progress=CO1
            sbCO1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    CO1 = progress
                    txtCO1.text = progress.toString()
                    databaseReference.child("Ngưỡng 1/Nồng độ CO").setValue(progress.toDouble())
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.drawable.bogochopthoai)
        }

        binding.tvCO2.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cài giới hạn")
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.co_t2, null)
            val txtCO2 = dialogView.findViewById<TextView>(R.id.txtCO2)
            val sbCO2 = dialogView.findViewById<SeekBar>(R.id.sbCO2)
            txtCO2.text=CO2.toString()
            sbCO2.progress=CO2
            sbCO2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    CO2 = progress
                    txtCO2.text = progress.toString()
                    databaseReference.child("Ngưỡng 2/Nồng độ CO").setValue(progress.toDouble())
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.drawable.bogochopthoai)
        }

        binding.tvBui1.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cài giới hạn")
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.pm25_t1, null)
            val txtBui1 = dialogView.findViewById<TextView>(R.id.txtP1)
            val sbBui1 = dialogView.findViewById<SeekBar>(R.id.sbPM251)
            txtBui1.text=Bui1.toString()
            sbBui1.progress=Bui1
            sbBui1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    Bui1=progress
                    txtBui1.text = progress.toString()
                    databaseReference.child("Ngưỡng 1/Bụi").setValue(progress.toDouble())
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.drawable.bogochopthoai)
        }

        binding.tvBui2.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Cài giới hạn")
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.pm25_t2, null)
            val txtBui2 = dialogView.findViewById<TextView>(R.id.txtP2)
            val sbBui2 = dialogView.findViewById<SeekBar>(R.id.sbPM252)
            txtBui2.text=Bui2.toString()
            sbBui2.progress=Bui2
            sbBui2.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    Bui2 = progress
                    txtBui2.text = progress.toString()
                    databaseReference.child("Ngưỡng 2/Bụi").setValue(progress.toDouble())
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
            builder.setView(dialogView)
            val dialog = builder.create()
            dialog.show()
            dialog.window?.setBackgroundDrawableResource(R.drawable.bogochopthoai)
        }

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val Tem1Data = snapshot.child("Trạm 1/Nhiệt độ").getValue(Double::class.java)
                val Hum1Data = snapshot.child("Trạm 1/Độ ẩm không khí").getValue(Int::class.java)
                val CO1Data  = snapshot.child("Trạm 1/Nồng độ CO").getValue(Double::class.java)
                val Bui1Data = snapshot.child("Trạm 1/Bụi").getValue(Double::class.java)
                val Tem2Data = snapshot.child("Trạm 2/Nhiệt độ").getValue(Double::class.java)
                val Hum2Data = snapshot.child("Trạm 2/Độ ẩm không khí").getValue(Int::class.java)
                val CO2Data  = snapshot.child("Trạm 2/Nồng độ CO").getValue(Double::class.java)
                val Bui2Data = snapshot.child("Trạm 2/Bụi").getValue(Double::class.java)

                binding.tvTemp1.setText(Tem1Data.toString()+"°C")
                binding.tvHum1.setText(Hum1Data.toString()+"%")
                binding.tvCO1.setText(CO1Data.toString())
                binding.tvBui1.setText(Bui1Data.toString())
                binding.tvTemp2.setText(Tem2Data.toString()+"°C")
                binding.tvHum2.setText(Hum2Data.toString()+"%")
                binding.tvCO2.setText(CO2Data.toString())
                binding.tvBui2.setText(Bui2Data.toString())

                if (Tem1Data != null && Tem1Data > Temp1) { //kiểm tra nhiệt độ hiện tại xem có > giá trị cài GH
                    binding.tvTemp1.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.red)) //
                    builder.setMessage("Cảnh báo: Trạm 1 nhiệt độ cao!")
                    builder.setNegativeButton("Thoát") { dialog, _ -> dialog.cancel() }
                    val alert = builder.create()
                    alert.show()
                    Canhbao()
                } else {
                    binding.tvTemp1.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.white))
                    StopCanhbao()
                }
                if (Tem2Data != null && Tem2Data > Temp2) {
                    binding.tvTemp2.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.red))
                    builder.setMessage("Cảnh báo: Trạm 2 nhiệt độ cao!")
                    builder.setNegativeButton("Thoát") { dialog, _ -> dialog.cancel() }
                    val alert = builder.create()
                    alert.show()
                    Canhbao()
                }else {
                    binding.tvTemp2.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.white))
                }
                if (Hum1Data != null && Hum1Data < Hum1) {
                    binding.tvHum1.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.red))
                    builder.setMessage("Cảnh báo: Trạm 1 độ ẩm thấp!")
                    builder.setNegativeButton("Thoát") { dialog, _ -> dialog.cancel() }
                    val alert = builder.create()
                    alert.show()
                    Canhbao()
                }else {
                    binding.tvHum1.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.white))
                }
                if (Hum2Data != null && Hum2Data < Hum2) {
                    binding.tvHum2.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.red))
                    builder.setMessage("Cảnh báo: Trạm 2 độ ẩm thấp!")
                    builder.setNegativeButton("Thoát") { dialog, _ -> dialog.cancel() }
                    val alert = builder.create()
                    alert.show()
                    Canhbao()
                }else {
                    binding.tvHum2.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.white))
                }
                if (CO1Data != null && CO1Data > CO1) {
                    binding.tvCO1.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.red))
                    builder.setMessage("Trạm 1 nồng độ CO cao!")
                    builder.setNegativeButton("Thoát") { dialog, _ -> dialog.cancel() }
                    val alert = builder.create()
                    alert.show()
                    Canhbao()
                }else {
                    binding.tvCO1.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.white))
                }
                if (CO2Data != null && CO2Data > CO2) {
                    binding.tvCO2.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.red))
                    builder.setMessage("Trạm 2 nồng độ CO cao!")
                    builder.setNegativeButton("Thoát") { dialog, _ -> dialog.cancel() }
                    val alert = builder.create()
                    alert.show()
                    Canhbao()
                }else {
                    binding.tvCO2.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.white))
                }
                if (Bui1Data != null && Bui1Data > Bui1) {
                    binding.tvBui1.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.red))
                    builder.setMessage("Trạm 1 mật độ bụi cao!")
                    builder.setNegativeButton("Thoát") { dialog, _ -> dialog.cancel() }
                    val alert = builder.create()
                    alert.show()
                    Canhbao()
                }else {
                    binding.tvBui1.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.white))
                }
                if (Bui2Data != null && Bui2Data > Bui2) {
                    binding.tvBui2.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.red))
                    builder.setMessage("Trạm 1 mật độ bụi cao!")
                    builder.setNegativeButton("Thoát") { dialog, _ -> dialog.cancel() }
                    val alert = builder.create()
                    alert.show()
                    Canhbao()
                }else {
                    binding.tvBui2.setTextColor(ContextCompat.getColor(this@giaodienchinh, R.color.white))
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun StopCanhbao() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun Canhbao() {
        if (mediaPlayer == null || !mediaPlayer!!.isPlaying) {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this@giaodienchinh, R.raw.warn)
            mediaPlayer?.start()
        }
    }
}
package com.example.cafe.MyAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.cafe.Activities.HomeActivity;
import com.example.cafe.Activities.PaymentActivity;
import com.example.cafe.DAO.BanDAO;
import com.example.cafe.DAO.HoaDonDAO;
import com.example.cafe.DTO.BanDTO;
import com.example.cafe.DTO.HoaDonDTO;
import com.example.cafe.Fragments.DisplayCategoryFragment;
import com.example.cafe.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class AdapterDisplayTable extends BaseAdapter implements View.OnClickListener {
    Context context;
    int layout;
    List<BanDTO> banAnDTOList;
    ViewHolder viewHolder;
    BanDAO banAnDAO;
    HoaDonDAO donDatDAO;
    FragmentManager fragmentManager;

    public AdapterDisplayTable(Context context, int layout, List<BanDTO> banAnDTOList){
        this.context = context;
        this.layout = layout;
        this.banAnDTOList = banAnDTOList;
        banAnDAO = new BanDAO(context);
        donDatDAO = new HoaDonDAO(context);
        fragmentManager = ((HomeActivity)context).getSupportFragmentManager();
    }

    @Override
    public int getCount() {
        return banAnDTOList.size();
    }

    @Override
    public Object getItem(int position) {
        return banAnDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return banAnDTOList.get(position).getMaBan();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, parent, false);

            viewHolder.imgBanAn = (ImageView) view.findViewById(R.id.img_customtable_BanAn);
            viewHolder.imgGoiMon = (ImageView) view.findViewById(R.id.img_customtable_GoiMon);
            viewHolder.imgThanhToan = (ImageView) view.findViewById(R.id.img_customtable_ThanhToan);
            viewHolder.imgAnNut = (ImageView) view.findViewById(R.id.img_customtable_AnNut);
            viewHolder.txtTenBanAn = (TextView) view.findViewById(R.id.txt_customtable_TenBanAn);
            viewHolder.cardView = view.findViewById(R.id.cardview);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (banAnDTOList.get(position).isDuocChon()) {
            HienThiButton(viewHolder);
        } else {
            AnButton(viewHolder);
        }

        BanDTO banAnDTO = banAnDTOList.get(position);

        String kttinhtrang = banAnDAO.LayTinhTrangBanTheoMa(banAnDTO.getMaBan());
        //đổi hình theo tình trạng
        if (kttinhtrang.equals("true")) {
            viewHolder.imgBanAn.setImageResource(R.drawable.ic_baseline_airline_seat_legroom_normal_40);
        } else {
            viewHolder.imgBanAn.setImageResource(R.drawable.ic_baseline_event_seat_40);
        }

        viewHolder.txtTenBanAn.setText(banAnDTO.getTenBan());
        viewHolder.imgBanAn.setTag(position);
        viewHolder.imgBanAn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện thay đổi trạng thái hiển thị của các ImageView dưới CardView
                if (viewHolder.imgGoiMon.getVisibility() == View.VISIBLE) {
                    AnButton(viewHolder);
                } else {
                    HienThiButton(viewHolder);
                }
            }
        });

        //sự kiện click
        viewHolder.imgGoiMon.setTag(position);
        viewHolder.imgThanhToan.setTag(position);
        viewHolder.imgAnNut.setTag(position);

        viewHolder.imgGoiMon.setOnClickListener(this);
        viewHolder.imgThanhToan.setOnClickListener(this);
        viewHolder.imgAnNut.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        int position = (int) v.getTag();
        ViewHolder viewHolder = (ViewHolder) ((View) v.getParent()).getTag();
        int maban = banAnDTOList.get(position).getMaBan();
        String tenban = banAnDTOList.get(position).getTenBan();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String ngaydat = dateFormat.format(calendar.getTime());

        if (id == R.id.img_customtable_BanAn) {
            int vitri = (int) v.getTag();
            banAnDTOList.get(vitri).setDuocChon(true);
            HienThiButton(viewHolder);
        } else if (id == R.id.img_customtable_AnNut) {
            AnButton(viewHolder);
        } else if (id == R.id.img_customtable_GoiMon) {
            Intent getIHome = ((HomeActivity) context).getIntent();
            int manv = getIHome.getIntExtra("manv", 0);
            String tinhtrang = banAnDAO.LayTinhTrangBanTheoMa(maban);

            if (tinhtrang.equals("false")) {
                // Thêm bảng gọi món và cập nhật tình trạng bàn
                HoaDonDTO donDatDTO = new HoaDonDTO();
                donDatDTO.setMaBan(maban);
                donDatDTO.setMaNV(manv);
                donDatDTO.setNgayDat(ngaydat);
                donDatDTO.setTinhTrang("false");
                donDatDTO.setTongTien("0");

                long ktra = donDatDAO.ThemDonDat(donDatDTO);
                banAnDAO.CapNhatTinhTrangBan(maban, "true");
                if (ktra == 0) {
                    Toast.makeText(context, context.getResources().getString(R.string.add_failed), Toast.LENGTH_SHORT).show();
                }
            }
            // Chuyển qua trang category
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            DisplayCategoryFragment displayCategoryFragment = new DisplayCategoryFragment();

            Bundle bDataCategory = new Bundle();
            bDataCategory.putInt("maban", maban);
            displayCategoryFragment.setArguments(bDataCategory);

            transaction.replace(R.id.contentView, displayCategoryFragment).addToBackStack("hienthibanan");
            transaction.commit();
        } else if (id == R.id.img_customtable_ThanhToan) {
            // Chuyển dữ liệu qua trang thanh toán
            Intent iThanhToan = new Intent(context, PaymentActivity.class);
            iThanhToan.putExtra("maban", maban);
            iThanhToan.putExtra("tenban", tenban);
            iThanhToan.putExtra("ngaydat", ngaydat);
            context.startActivity(iThanhToan);
        }

    }
    private void HienThiButton(ViewHolder viewHolder) {
        viewHolder.imgGoiMon.setVisibility(View.VISIBLE);
        viewHolder.imgThanhToan.setVisibility(View.VISIBLE);
        viewHolder.imgAnNut.setVisibility(View.VISIBLE);
    }

    private void AnButton(ViewHolder viewHolder) {
        viewHolder.imgGoiMon.setVisibility(View.INVISIBLE);
        viewHolder.imgThanhToan.setVisibility(View.INVISIBLE);
        viewHolder.imgAnNut.setVisibility(View.INVISIBLE);
    }

    public class ViewHolder{
        ImageView imgBanAn, imgGoiMon, imgThanhToan, imgAnNut;
        TextView txtTenBanAn;
        RelativeLayout cardView;
    }
}

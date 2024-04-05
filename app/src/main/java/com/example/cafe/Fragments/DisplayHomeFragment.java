package com.example.cafe.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cafe.Activities.AddCategoryActivity;
import com.example.cafe.Activities.HomeActivity;
import com.example.cafe.DAO.HoaDonDAO;
import com.example.cafe.DAO.LoaiDAO;
import com.example.cafe.DTO.HoaDonDTO;
import com.example.cafe.DTO.LoaiDTO;
import com.example.cafe.MyAdapter.AdapterRecycleViewCategory;
import com.example.cafe.MyAdapter.AdapterRecycleViewStatistic;
import com.example.cafe.R;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class DisplayHomeFragment extends Fragment implements View.OnClickListener {
    RecyclerView rcv_displayhome_LoaiMon, rcv_displayhome_DonTrongNgay;
    RelativeLayout layout_displayhome_ThongKe, layout_displayhome_XemBan, layout_displayhome_XemMenu, layout_displayhome_XemNV;
    TextView txt_displayhome_ViewAllCategory, txt_displayhome_ViewAllStatistic;
    LoaiDAO loaiMonDAO;
    HoaDonDAO donDatDAO;
    int maquyen = 0;
    SharedPreferences sharedPreferences;
    List<LoaiDTO> loaiMonDTOList;
    List<HoaDonDTO> donDatDTOS;
    AdapterRecycleViewCategory adapterRecycleViewCategory;
    AdapterRecycleViewStatistic adapterRecycleViewStatistic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.displayhome_layout, container, false);
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Trang chủ");
        setHasOptionsMenu(true);

        //region Lấy dối tượng view
        rcv_displayhome_LoaiMon = (RecyclerView) view.findViewById(R.id.rcv_displayhome_LoaiMon);
        rcv_displayhome_DonTrongNgay = (RecyclerView) view.findViewById(R.id.rcv_displayhome_DonTrongNgay);
        layout_displayhome_ThongKe = (RelativeLayout) view.findViewById(R.id.layout_displayhome_ThongKe);
        layout_displayhome_XemBan = (RelativeLayout) view.findViewById(R.id.layout_displayhome_XemBan);
        layout_displayhome_XemMenu = (RelativeLayout) view.findViewById(R.id.layout_displayhome_XemMenu);
        layout_displayhome_XemNV = (RelativeLayout) view.findViewById(R.id.layout_displayhome_XemNV);
        txt_displayhome_ViewAllCategory = (TextView) view.findViewById(R.id.txt_displayhome_ViewAllCategory);
        txt_displayhome_ViewAllStatistic = (TextView) view.findViewById(R.id.txt_displayhome_ViewAllStatistic);
        //endregion

        //lấy file sharepreference từ trang đăng nhập
        Context context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("info", Context.MODE_PRIVATE);
        maquyen = sharedPreferences.getInt("maquyen", 0);

        //khởi tạo kết nối
        loaiMonDAO = new LoaiDAO(getActivity());
        donDatDAO = new HoaDonDAO(getActivity());

        HienThiDSLoai();
        HienThiDonTrongNgay();

        layout_displayhome_ThongKe.setOnClickListener(this);
        layout_displayhome_XemBan.setOnClickListener(this);
        layout_displayhome_XemMenu.setOnClickListener(this);
        layout_displayhome_XemNV.setOnClickListener(this);
        txt_displayhome_ViewAllCategory.setOnClickListener(this);
        txt_displayhome_ViewAllStatistic.setOnClickListener(this);

        return view;
    }

    private void HienThiDSLoai() {
        rcv_displayhome_LoaiMon.setHasFixedSize(true);
        rcv_displayhome_LoaiMon.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        loaiMonDTOList = loaiMonDAO.LayDSLoaiMon();
        adapterRecycleViewCategory = new AdapterRecycleViewCategory(getActivity(), R.layout.custom_layout_displaycategory, loaiMonDTOList);
        rcv_displayhome_LoaiMon.setAdapter(adapterRecycleViewCategory);
        adapterRecycleViewCategory.notifyDataSetChanged();
    }

    private void HienThiDonTrongNgay() {
        rcv_displayhome_DonTrongNgay.setHasFixedSize(true);
        rcv_displayhome_DonTrongNgay.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String ngaydat = dateFormat.format(calendar.getTime());

        donDatDTOS = donDatDAO.LayDSDonDatNgay(ngaydat);
        adapterRecycleViewStatistic = new AdapterRecycleViewStatistic(getActivity(), R.layout.custom_layout_displaystatistic, donDatDTOS);
        rcv_displayhome_DonTrongNgay.setAdapter(adapterRecycleViewStatistic);
        adapterRecycleViewCategory.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.navigation_view_trangchu);
        if (id == R.id.layout_displayhome_ThongKe || id == R.id.txt_displayhome_ViewAllStatistic) {
            FragmentTransaction tranDisplayStatistic = getActivity().getSupportFragmentManager().beginTransaction();
            tranDisplayStatistic.replace(R.id.contentView, new DisplayStatisticFragment());
            tranDisplayStatistic.addToBackStack(null);
            tranDisplayStatistic.commit();
            navigationView.setCheckedItem(R.id.nav_statistic);
        } else if (id == R.id.layout_displayhome_XemBan) {
            FragmentTransaction tranDisplayTable = getActivity().getSupportFragmentManager().beginTransaction();
            tranDisplayTable.replace(R.id.contentView, new DisplayTableFragment());
            tranDisplayTable.addToBackStack(null);
            tranDisplayTable.commit();
            navigationView.setCheckedItem(R.id.nav_table);
        } else if (id == R.id.layout_displayhome_XemMenu) {
            Intent iAddCategory = new Intent(getActivity(), AddCategoryActivity.class);
            startActivity(iAddCategory);
            navigationView.setCheckedItem(R.id.nav_category);
        } else if (id == R.id.layout_displayhome_XemNV) {
            if (maquyen == 1) {
                FragmentTransaction tranDisplayStaff = getActivity().getSupportFragmentManager().beginTransaction();
                tranDisplayStaff.replace(R.id.contentView, new DisplayStaffFragment());
                tranDisplayStaff.addToBackStack(null);
                tranDisplayStaff.commit();
                navigationView.setCheckedItem(R.id.nav_staff);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "Bạn không có quyền truy cập", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.txt_displayhome_ViewAllCategory) {
            FragmentTransaction tranDisplayCategory = getActivity().getSupportFragmentManager().beginTransaction();
            tranDisplayCategory.replace(R.id.contentView, new DisplayCategoryFragment());
            tranDisplayCategory.addToBackStack(null);
            tranDisplayCategory.commit();
            navigationView.setCheckedItem(R.id.nav_category);
        }

    }
}

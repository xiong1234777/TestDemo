package com.xiong.mdapplication.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import com.xiong.mdapplication.utils.UriUtil;

import java.io.File;

/**
 * 弹出对话选择拍照与相册，并提供方法处理好返回的结果
 * 
 * @author Xiong
 * 
 */
public class ChoosePhotoDao implements OnClickListener {

	private static final int REQUEST_TAKE_PHOTO = 1;
	private static final int REQUEST_FROM_ALBUM = 2;
	private Fragment mFragment;
	private Activity mActivity;
	protected Uri imageUri;
	private AlertDialog dialog;
	private Context mContext;
	private boolean isMulti;
	/**
	 * @param isMulti	是否多选 
	 */
	public ChoosePhotoDao(Activity mActivity, boolean isMulti) {
		super();
		this.mActivity = mActivity;
		mContext = mActivity;
		this.isMulti = isMulti;
	}

	/**
	 * @param isMulti	是否多选 
	 */
	public ChoosePhotoDao(Fragment mFragment, boolean isMulti) {
		super();
		this.mFragment = mFragment;
		mContext = mFragment.getActivity();
		this.isMulti = isMulti;
	}

	public void show() {
		if(dialog==null)
		dialog = new AlertDialog.Builder(mContext).setItems(
				new String[]{"\t\t\t打开相机", "\t\t从相册选取"}, this).create();
		dialog.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		String action = null;
		String type = null;
		int requestCode = -1;
		switch (which) {
			case 0 :
				// 拍照
				// 创建File对象，用于存储选择后的图片
				File outputImage = new File(
						Environment.getExternalStorageDirectory(),
						"tempImage.jpg");
				try {
					if (outputImage.exists()) {
						outputImage.delete();
					}
					outputImage.createNewFile();
				} catch (Exception e) {
					e.printStackTrace();
				}
				imageUri = Uri.fromFile(outputImage);
				action = "android.media.action.IMAGE_CAPTURE";
				requestCode =  REQUEST_TAKE_PHOTO;
				break;
			case 1 :
				if (!isMulti) {
					// 系统相册
					action = Intent.ACTION_PICK;
					type = "image/*";
					requestCode =  REQUEST_FROM_ALBUM;
				} else {

					// 多选图片的页面，既然是选头像，就要限制只能选择一张
//					if (mActivity == null) {
//
//						SystemTools.gotoTempActivity(mFragment,
//								MultiPhotoFrag.class.getName(),
//								 REQUEST_MULTI_PHOTO);
//					} else {
//						SystemTools.gotoTempActivity(mActivity,
//								MultiPhotoFrag.class.getName(),
//								 REQUEST_MULTI_PHOTO);
//
//					}
//					return;
				}
			default :
				break;
		}
		Intent intent = new Intent(action);
		// 图片输出到指定目录对应的URI
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
		intent.setType(type);

		if (mActivity == null) {

			mFragment.startActivityForResult(intent, requestCode);
		} else {
			mActivity.startActivityForResult(intent, requestCode);
		}
	}
	/**
	 * 用于接收处理完成的结果
	 * 
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 * @return
	 */
	public String onResult(int requestCode, int resultCode, Intent data) {
		if(Activity.RESULT_OK!=resultCode)return null;
		// 如果结果码不是OK，就不会有数据传递回来，直接结束
		Uri uri = null;
		switch (requestCode) {
			case  REQUEST_TAKE_PHOTO :
				uri = imageUri;

				break;
			case  REQUEST_FROM_ALBUM :
				if (data == null) return null;
				uri = data.getData();

				break;
//			case  REQUEST_MULTI_PHOTO :
//				// 接收多选图片的数据
//				// 如果结果码匹配成功，才会有图片回来
//				String[] imagePaths = data.getStringArrayExtra("imagePaths");
//				return imagePaths;
			default :
				break;
		}

		String imagePath = null;
		if (uri != null) {
			if (uri.getScheme().equals("content")) {
				imagePath = UriUtil.getDataColumn(mContext, uri, null, null);
			} else {
				imagePath = uri.getPath();
			}

		}


		return imagePath;
	}

}
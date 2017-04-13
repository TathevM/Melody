package com.team_red.melody.filemanager;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.team_red.melody.app.MelodyApplication;
import com.team_red.melody.models.Composition;
import com.team_red.melody.models.MelodyAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import static com.team_red.melody.filemanager.MelodyFileManager.EXPORTED_FILE_DIRECTORY;

public class MelodyPDF {

    private static final String HANDLER_THREAD = "handlerThread";
    private static final String PDF_DIR = "/Sheets";

    private Composition mComposition;
    private IOnPDFExportFinishedListener mOnPDFExportFinishedListener;

    public MelodyPDF() {
    }

    private void exportToFile(Bitmap bitmap){
        Document doc = new Document();
        try {
            File root = android.os.Environment.getExternalStorageDirectory();
            String username = MelodyApplication.getLoggedInUser().getUserName();
            File dir = new File(root.getAbsolutePath() + EXPORTED_FILE_DIRECTORY + PDF_DIR);
            dir.mkdirs();
            File pdfFile = new File(dir, username + " - " + mComposition.getCompositionName() + ".pdf");

            FileOutputStream outputStream = new FileOutputStream(pdfFile);
            PdfWriter.getInstance(doc, outputStream);
            doc.open();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image image = Image.getInstance(stream.toByteArray());
            image.scalePercent(45);
            image.setAlignment(Image.MIDDLE);
            doc.add(image);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            doc.close();
            mOnPDFExportFinishedListener.onPdfExportFinished();
        }
    }

    public void export(TextView textView, RecyclerView view, Composition composition, IOnPDFExportFinishedListener callback) {
        mOnPDFExportFinishedListener = callback;
        mComposition = composition;
        HandlerThread handlerThread = new HandlerThread(HANDLER_THREAD);
        handlerThread.start();

        Handler h = new Handler(handlerThread.getLooper());
        PdfExporter pdfExporter = new PdfExporter(textView, view);
        h.postDelayed(pdfExporter, 500);
    }

    private class PdfExporter implements Runnable {

        RecyclerView view;
        TextView compositionNameView;

        PdfExporter(TextView compositionNameView, RecyclerView view) {
            this.view = view;
            this.compositionNameView = compositionNameView;
        }

        @Override
        public void run() {
            MelodyAdapter adapter = (MelodyAdapter) view.getAdapter();
            Bitmap bigBitmap = null;
            if(adapter != null){
                int size = adapter.getItemCount();
                int height = 0;
                Paint paint = new Paint();
                int iHeight = 0;
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

                final int cacheSize = maxMemory / 8;
                LruCache<String, Bitmap> bitmapCache = new LruCache<>(cacheSize);

                compositionNameView.setDrawingCacheEnabled(true);
                compositionNameView.buildDrawingCache();
                Bitmap labelDrawingCache = compositionNameView.getDrawingCache();
                if(labelDrawingCache != null)
                    bitmapCache.put(String.valueOf(0), labelDrawingCache);
                height += compositionNameView.getMeasuredHeight();

                for (int i = 0; i < size; i++) {
                    RecyclerView.ViewHolder holder = adapter.createViewHolder(view, adapter.getItemViewType(i));
                    adapter.onBindViewHolder(holder, i);
                    holder.itemView.measure(View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY),
                            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                    holder.itemView.layout(0, 0, holder.itemView.getMeasuredWidth(), holder.itemView.getMeasuredHeight());
                    holder.itemView.setDrawingCacheEnabled(true);
                    holder.itemView.buildDrawingCache();
                    Bitmap drawingCache = holder.itemView.getDrawingCache();
                    if (drawingCache != null) {

                        bitmapCache.put(String.valueOf(i+1), drawingCache);
                    }
//                holder.itemView.setDrawingCacheEnabled(false);
//                holder.itemView.destroyDrawingCache();
                    height += holder.itemView.getMeasuredHeight();
                }
                bigBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), height, Bitmap.Config.ARGB_8888);
                Canvas bigCanvas = new Canvas(bigBitmap);
                bigCanvas.drawColor(Color.WHITE);

                for (int i = 0; i < size; i++) {
                    Bitmap bitmap = bitmapCache.get(String.valueOf(i));
                    bigCanvas.drawBitmap(bitmap, 0f, iHeight, paint);
                    iHeight += bitmap.getHeight();
                    bitmap.recycle();
                }
            }
            exportToFile(bigBitmap);
        }
    }

    public interface IOnPDFExportFinishedListener {
        void onPdfExportFinished();
    }
}

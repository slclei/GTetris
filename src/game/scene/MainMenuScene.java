package game.scene;

import game.Game;
import game.GameSetting;
import game.U5;
import game.res.TextRes;
import game.ui.DialogResultListener;
import game.ui.GButton;
import game.ui.GDialogPanel;
import game.ui.GDialogPanel.DialogButton;
import game.ui.GLabel;
import game.ui.GScene;
import game.ui.MessageBox;
import game.ui.SceneManager;
import game.ui.UISetting;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JComponent;
import javax.swing.JLabel;

public class MainMenuScene extends GScene
	implements KeyListener, ActionListener
{
	public enum SceneAction
	{
		StartGame("StartName"),
		ContinueGame("Continue"),
		About("About"),
		Quit("Quit")
		;
		
		private final String text;
		private static Map<String, SceneAction> actionMap;
		static
		{
			actionMap = new HashMap<String, SceneAction>();
			SceneAction[] array = SceneAction.values();
			for (int i = 0; i < array.length; i++)
			{
				actionMap.put(array[i].toString(), array[i]);
			}
		}
		
		private SceneAction(final String text)
		{
			this.text = text;
		}

	    @Override
	    public String toString() {
	        return text;
	    }
	    
	    public static SceneAction getEnum(String name)
	    {
	    	return actionMap.get(name);
	    }
	}

	public MainMenuScene()
	{
		super();
		setName("MainMenuScene");
		addKeyListener(this);
		
		// 添加菜单
		{
			int x = getWidth() - 220;
			int y = getHeight() - 180;
			GButton button;
			EnumMap<SceneAction, String> menuMap = new EnumMap<SceneAction, String>(SceneAction.class);
			
			menuMap.put(SceneAction.StartGame, "Start Game");
			menuMap.put(SceneAction.ContinueGame, "Continue");
			menuMap.put(SceneAction.Quit, "Quit");
			menuMap.put(SceneAction.About, "About");

			Iterator<Entry<SceneAction, String>> it = menuMap.entrySet().iterator();
		    while (it.hasNext())
		    {
		        Map.Entry<SceneAction, String> pairs = it.next();
		        
		        button = new GButton();
		        button.setName(pairs.getKey().toString());
				button.setText(pairs.getValue().toString());
				button.setBounds(x , y += 30, 200, 24);
				button.addActionListener(this);
				add(button);
		        
		        it.remove(); // avoids a ConcurrentModificationException
		    }
		}
		
		// Label
		{
			GLabel label;
			String text;
			
			// 版本号
			label = new GLabel();
			text = "%s v %s";
			text = String.format(text, GameSetting.title,GameSetting.Version);
			label.setText(text);
			
			label.setFont(UISetting.ChineseFont.deriveFont(Font.PLAIN, 12));
			label.setVerticalAlignment(JLabel.TOP);
			label.setHorizontalAlignment(JLabel.LEFT);
			label.setSize(label.getPreferredSize());
			label.setLocation(getWidth() - 100, getHeight() - label.getHeight());
			add(label);
			
			// logo
			label = new GLabel();
			label.setText(GameSetting.logo);
			label.setFont(UISetting.LogoFont.deriveFont(Font.PLAIN, 60));
			label.setSize(label.getPreferredSize());
			label.setLocation((getWidth() - label.getWidth())/2, 120);
			add(label);
		}
	}

	public void enterStage(SceneManager sceneManager)
	{	
		super.enterStage(sceneManager);
		MessageBox.ShowNotify("我的网站 http://wener.me",0);
	}
	
	@Override
	public void keyPressed(KeyEvent e)
	{
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		
		String cmd;
		if(e.getSource() instanceof JComponent)
		{
			cmd = ((JComponent) e.getSource()).getName();
		}else {
			return;
		}
		
		doAction(SceneAction.getEnum(cmd));
	}

	private void doAction(SceneAction cmd)
	{
		if (cmd == null)
			return;
		
		switch (cmd)
		{
			case StartGame:
				sceneManager.setSceneInfo("StartGame").showScene("PlayScene");
				break;
			
			case ContinueGame:
				sceneManager.setSceneInfo("Continue").showScene("PlayScene");
				break;
				
			case About:
				MessageBox.Build("About", TextRes.About.getText(),null).Show();
				break;
				
			case Quit:
				MessageBox.Build("Sure to quit ?", "亲,你确定要退出么?",
						new DialogButton[]{DialogButton.Yes, DialogButton.No},
						new DialogResultListener()
						{
							
							@Override
							public void OnDialogButtonClick(GDialogPanel dialogPanel,
									DialogButton clicked)
							{
								if (clicked == DialogButton.Yes)
								{
									Game.QuitGame();
								}
							}
						}).Show();
				break;
			
			default:
			break;
		}
	}

}

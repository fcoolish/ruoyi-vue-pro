package cn.iocoder.yudao.adminserver.modules.bpm.service.task.listener;

import cn.iocoder.yudao.adminserver.modules.bpm.dal.dataobject.task.BpmProcessInstanceExtDO;
import cn.iocoder.yudao.adminserver.modules.bpm.service.task.BpmProcessInstanceService;
import org.activiti.api.model.shared.event.RuntimeEvent;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.events.ProcessRuntimeEvent;
import org.activiti.api.process.runtime.events.listener.ProcessEventListener;
import org.activiti.api.process.runtime.events.listener.ProcessRuntimeEventListener;
import org.activiti.api.task.model.events.TaskRuntimeEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 监听 {@link ProcessInstance} 的开始与完成，创建与更新对应的 {@link BpmProcessInstanceExtDO} 记录
 *
 * @author 芋道源码
 */
@Component
public class BpmProcessInstanceEventListener<T extends RuntimeEvent<?, ?>>
        implements ProcessRuntimeEventListener<T> {

    @Resource
    @Lazy // 解决循环依赖
    private BpmProcessInstanceService processInstanceService;

    @Override
    @SuppressWarnings("unchecked")
    public void onEvent(T rawEvent) {
        // 由于 ProcessRuntimeEventListener 无法保证只监听 ProcessRuntimeEvent 事件，所以通过这样的方式
        if (!(rawEvent instanceof ProcessRuntimeEvent)) {
            return;
        }
        ProcessRuntimeEvent<ProcessInstance> event = (ProcessRuntimeEvent<ProcessInstance>) rawEvent;

        // 创建时，插入拓展表
        if (event.getEventType() == ProcessRuntimeEvent.ProcessEvents.PROCESS_CREATED) {
            processInstanceService.createProcessInstanceExt(event.getEntity());
            return;
        }
        // 取消时，更新拓展表为取消
        if (event.getEventType() == ProcessRuntimeEvent.ProcessEvents.PROCESS_CANCELLED) {
            processInstanceService.updateProcessInstanceExtCancel(event.getEntity());
            return;
        }
        // 完成时，更新拓展表为已完成
        if (event.getEventType() == ProcessRuntimeEvent.ProcessEvents.PROCESS_COMPLETED) {
            processInstanceService.updateProcessInstanceExtComplete(event.getEntity());
            return;
        }

        // 其它事件，进行更新拓展表
        processInstanceService.updateProcessInstanceExt(event.getEntity());
    }

}
